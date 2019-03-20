#!/home/matthes/anaconda2/envs python

import cv2
import numpy as np
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from collections import Counter
from scipy.misc import imread, imresize, imsave

def find_vector_set(diff_image, new_size):
   
    i = 0
    j = 0
    vector_set = np.zeros(((new_size[0] * new_size[1]) / 25, 25))
    while i < vector_set.shape[0]:
        while j < new_size[0]:
            k = 0
            while k < new_size[1]:
                block   = diff_image[j:j+5, k:k+5]
                feature = block.ravel()
                vector_set[i, :] = feature
                k = k + 5
            j = j + 5
        i = i + 1
        
    print '\nvector_set shape', vector_set.shape
            
    mean_vec   = np.mean(vector_set, axis = 0)    
    vector_set = vector_set - mean_vec
    
    return vector_set, mean_vec
    
  
def find_FVS(EVS, diff_image, mean_vec, new):
    
    i = 2 
    feature_vector_set = []
    
    while i < new[0] - 2:
        j = 2
        while j < new[1] - 2:
            block = diff_image[i-2:i+3, j-2:j+3]
            feature = block.flatten()
            feature_vector_set.append(feature)
            j = j+1
        i = i+1
        
    FVS = np.dot(feature_vector_set, EVS)
    FVS = FVS - mean_vec
    print "\nfeature vector space size", FVS.shape
    return FVS

def clustering(FVS, components, new):
    
    kmeans = KMeans(components, verbose = 0)
    kmeans.fit(FVS)
    output = kmeans.predict(FVS)
    count  = Counter(output)

    least_index = min(count, key = count.get)            
    change_map  = np.reshape(output,(new[0] - 4, new[1] - 4))
    
    return least_index, change_map

   
def find_PCAKmeans(imagepath1, imagepath2):
    
    print 'Operating'
    
    image1 = imread(imagepath1)
    print(image1.shape[1])
    print(image1.shape[0])
    image2 = imread(imagepath2)
    print(image2.shape[1])
    print(image2.shape[0])
    
    new_size = np.asarray(image1.shape) / 5 * 5
    print('\new size ', np.asarray(image1.shape))
    print('\new size ', np.asarray(image1.shape) / 5)
    print('\new size ', np.asarray(image1.shape) / 5 * 5)
 
    image1 = image1.astype(np.int16)
    image2 = image2.astype(np.int16)
    
    diff_image = abs(image2 - image1)   
    imsave('data/diff.jpg', diff_image)
    shape = diff_image.shape[0]

    print '\nBoth images resized to ',diff_image.shape[0]
        
    vector_set, mean_vec = find_vector_set(diff_image, new_size)
    
    pca = PCA()
    pca.fit(vector_set)
    EVS = pca.components_
        
    FVS     = find_FVS(EVS, diff_image, mean_vec, new_size)
    
    print '\ncomputing k means'
    
    components = 4
    least_index, change_map = clustering(FVS, components, new_size)
    
    change_map[change_map == least_index] = 255
    change_map[change_map != 255] = 0
    
    change_map = change_map.astype(np.uint8)
    kernel     = np.asarray(((0,0,1,0,0),
                             (0,1,1,1,0),
                             (1,1,1,1,1),
                             (0,1,1,1,0),
                             (0,0,1,0,0)), dtype=np.uint8)
    cleanChangeMap = cv2.erode(change_map,kernel)
    imsave("data/changemap.jpg", change_map)
    imsave("data/cleanchangemap.jpg", cleanChangeMap)

    #finding the loss from the change_map image
    n_white_pix = np.sum(cleanChangeMap == 255)
    n_black_pix = np.sum(cleanChangeMap == 0)
    blackpix = float(n_black_pix)
    whitepix = float(n_white_pix)
    loss = (whitepix/blackpix)*100
   
    # Writing the loss to file
    f=open("data/yield_loss.txt","w+")
    f.write("white pixels:\n")
    f.write('{:03f}\n'.format(whitepix))
    f.write("black pixels:\n")
    f.write('{:03f}\n'.format(blackpix))
    f.write("Loss is:\n")
    f.write('{:03f}\n'.format(loss))
    f.close()

    
if __name__ == "__main__":
    print('test')
    a = 'data/nvdiDay1.jpg'
    b = 'data/nvdiDay2.jpg'
    find_PCAKmeans(a,b)    
    print("finished PCA")
    
