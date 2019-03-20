# Farmero App

Submission for TU Berlin by Matthes Krull, Ajay Kumar Mandapati, Kashika Manocha 


## Content


The App is divided into four parts: 1. App (Frontend, Android Studio) 2. Backend (NodeJS, Python) 3. Disease prediction (Python) and 4. Docker

## 1. App (Frontend, Android Studio)


The Android Studio project is located under (Disease prediction models are not included due to their size):

```
app/farmero
```
The relevant Android Studio Java Classes are located under:

```
app/farmero/app/src/main/java/com/example/matthes/farmero
```
The apk file (just download and install on Android Phones) is located under:
```
https://drive.google.com/open?id=1Yst1J-KTq20vNTUjDoNVezxotOsCipac
```

## 2. Backend (NodeJS, Python, Google-Cloud)
The contents of the backend can be found in the docker_backed folder:
- Files needed to build the Nodejs server
- Output files from the Nodejs server
- Steps to create the Google-cloud instance and moving docker to the cloud

## 3. Disease prediction (Python)
Alexnet model, Mobilenet Model
- Converting this to TF-lite can be found in the folder keras_to_tensorflow
- Converting the Alexnet model to Mobilenet can be found in alexnet_to_mobilenet

## 3. Docker
Docker instructions to build the image can be found in the folder docker_backend:
- Docker_instructions
- Dockerfile
- Please find the pre-built image at this URL: https://cloud.docker.com/u/akumarmandapati3067/repository/docker/akumarmandapati3067/farmero_final

