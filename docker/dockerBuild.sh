#!/bin/sh

# Build the application project
mvn clean install

# Build the image from the application
docker build -t car-app .