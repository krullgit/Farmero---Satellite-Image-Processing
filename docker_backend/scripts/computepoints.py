import ee
from io import BytesIO
import zipfile
from zipfile import ZipFile
from urllib.request import urlopen
import re
import sys
import json
import numpy as np

import fiona
from PIL import Image
import rasterio
import rasterio.mask
from osgeo import gdal

# In this case the nodejs server provides the coordinates in an argument
coord = json.loads(sys.argv[1])
geoms = [{'type': 'Polygon', 'coordinates': [[tuple(l) for l in coord]] }]

with rasterio.open('data/nvdiDay1.tif') as src:
    out_image, out_transform = rasterio.mask.mask(src, geoms, crop=True)
    out_meta = src.meta

with rasterio.open('data/cleanchangemap.jpg') as src:
    rgb = np.float32(src.read())


out_meta.update({"driver": "GTiff",
                "height": rgb.shape[1],
                "width": rgb.shape[2],
                "transform": out_transform})

with rasterio.open('data/changemapWithGeo.tif', "w", **out_meta) as dest:
    dest.write(rgb)


from affine import Affine
from pyproj import Proj, transform

fname = 'data/changemapWithGeo.tif'

# Read raster
with rasterio.open(fname) as r:
    T0 = r.transform  # upper-left pixel corner affine transform
    p1 = Proj(r.crs)
    A = r.read()  # pixel values

# All rows and columns
AoneDim = A[0].reshape(-1)
cols, rows = np.meshgrid(np.arange(A.shape[2]), np.arange(A.shape[1]))


# Get affine transform for pixel centres
T1 = T0 * Affine.translation(0.5, 0.5)
# Function to convert pixel row/column index (from 0) to easting/northing at centre
rc2en = lambda r, c: (c, r) * T1

# All eastings and northings (there is probably a faster way to do this)
eastings, northings = np.vectorize(rc2en, otypes=[np.float, np.float])(rows, cols)
print(eastings[0])
print(eastings.shape)
print(northings.shape)

# Project all longitudes, latitudes
p2 = Proj(proj='latlong',datum='WGS84')
longs, lats = transform(p1, p2, eastings, northings)

longsOneDim = longs.reshape(-1)
latsOneDim = lats.reshape(-1)
AoneDim = A[0].reshape(-1)

longsOneDimOnlyWhite = []
latsOneDimDimOnlyWhite = []

def f(idx, j):
    if(AoneDim[idx]>100):
        return j

for idx, j in enumerate(longsOneDim):
    amIWhite = f(idx, j)
    if(amIWhite != None):
        longsOneDimOnlyWhite.append(amIWhite)


for idx, j in enumerate(latsOneDim):
    amIWhite = f(idx, j)
    if(amIWhite != None):
        latsOneDimDimOnlyWhite.append(amIWhite)


f= open("data/points.txt","w+")

for idx, j in enumerate(longsOneDimOnlyWhite):
    f.write(str(j))
    f.write(",")
    f.write(str(latsOneDimDimOnlyWhite[idx]))
    f.write("#")
    f.write("\n")


f.close()

#Caculate difference between NDVI images
i1 = Image.open("data/nvdiDay1_color.png")
i2 = Image.open("data/nvdiDay2_color.png")
assert i1.mode == i2.mode, "Different kinds of images."
assert i1.size == i2.size, "Different sizes."

pairs = zip(i1.getdata(), i2.getdata())
if len(i1.getbands()) == 1:
    # for gray-scale jpegs
    dif = sum(abs(p1-p2) for p1,p2 in pairs)
else:
    dif = sum(abs(c1-c2) for p1,p2 in pairs for c1,c2 in zip(p1,p2))

ncomponents = i1.size[0] * i1.size[1] * 3
print("Difference (percentage):", (dif / 255.0 * 100) / ncomponents)
#write the value to a file used in the app
f=open("data/yield_loss.txt","a+")
f.write("Difference in NDVI images from Day-1 to Day-2 \n")
f.write('{:03f}\n'.format((dif / 255.0 * 100) / ncomponents))
f.close()

#Creating zip archive for app consumption
z = zipfile.ZipFile("data/app.zip", "w")
z.write("data/nvdiDay1_color.png")
z.write("data/nvdiDay2_color.png")
z.write("data/stack_day1.png")
z.write("data/stack_day2.png")
z.write("data/yield_loss.txt")
z.write("data/points.txt")
z.close()
