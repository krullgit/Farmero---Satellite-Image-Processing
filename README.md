# Farmero App

<p align="center"> 
<img src="farmero.png">
</p>

Submission by Matthes Krull, Ajay Kumar Mandapati, Kashika Manocha 


## Content

The project is divided into four parts: 0. Documentation 1. App (Frontend, Android Studio) 2. Backend (NodeJS, Python) 3. Disease prediction (Tensorflow, Keras, Python) and 4. Docker(Hosting the backend on GCP)

## 0. Documentation

Installation instructions, use cases, data for testing, list of devices:

```
https://docs.google.com/document/d/12zU_qe97C54v56L5b--gz6MDRKUx1uSTPHwWqAvEVtQ/edit?usp=sharing
```
Testing vedio and Data for ML part.
```
https://drive.google.com/drive/folders/1dh7QRQPWtkv-3Kq2t7QeuO7Pr-mWBzYi?usp=sharing
```

Architecture
```
https://docs.google.com/document/d/1ht4LUhrgRNuauB-o0SNxFnezEgDebQo332tsWxCEhZw/edit?usp=sharing
```

## 0.1 AI-Machine Learning part

Field Risk Prediction Report
```
https://docs.google.com/document/d/1kjCrEjYT7wUjAmj_yfo9747YEgxNifbBahLf5nSGl4o/edit?usp=sharing
```

Plant Disease Prediction Report
```
https://docs.google.com/document/d/11wOL8-AUdbCWhy4Ii85pJ-BHtwd0qRDjvr8RbxV7yDk/edit?usp=sharing
```


## 1. App (Frontend, Android Studio)


The apk file (Please see installation instructions under "documentation") is located under:
```
https://drive.google.com/file/d/1L0T9WyWfxgZUjypk5S9C5cAfiQ0s5TNy/view?usp=sharing
```

Android Studio project (just open as existing project in Android Studio):

```
https://drive.google.com/file/d/15kx5okl4lQYOwIX815osJ4Q3o9s2ujC4/view?usp=sharing
```
The relevant Android Studio Java Classes are located under:

```
app/farmero/app/src/main/java/com/example/matthes/farmero
```
The relevant test-cases can be located here:

```
https://drive.google.com/drive/folders/1dh7QRQPWtkv-3Kq2t7QeuO7Pr-mWBzYi?usp=sharing
```


## 2. Backend (NodeJS, Python, Google-Cloud)
The contents of the backend can be found in the docker_backed folder:
- Files needed to build the Nodejs server
- Output files from the Nodejs server
- Steps to create the Google-cloud instance and moving docker to the cloud

## 3. Disease prediction (Tensorflow, Keras, Python)
- Navigate to the plant_disease_detection folder to find the ipython notebook which has the description of the Alexnet implemented on the dataset ( Please find the dataset in this location: https://drive.google.com/drive/folders/1dh7QRQPWtkv-3Kq2t7QeuO7Pr-mWBzYi?usp=sharing).
- Python code for relating diseases with the possible mitigation cases can be found in this folder.

Alexnet model, Mobilenet Model
- Converting this to TF-lite can be found in the folder keras_to_tensorflow
- Converting the Alexnet model to Mobilenet can be found in alexnet_to_mobilenet

## 4. Docker
Docker instructions to build the image can be found in the folder docker_backend:
- Docker_instructions
- Dockerfile
- Please find the pre-built image at this URL:
https://cloud.docker.com/u/akumarmandapati3067/repository/docker/akumarmandapati3067/farmero_final

