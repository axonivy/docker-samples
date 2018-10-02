#!/bin/bash

# sample call: ./downloadApp.sh ch/ivyteam/ivy/project/demo/ci/deploy/application application zip

# Maven artifact
groupId=$1
artifactId=$2
type=$3

# resolve URI
server=https://repo.axonivy.com/artifactory/libs-snapshot
path=$server/$groupId/$artifactId
version=$(curl -s $path/maven-metadata.xml | grep "<version>" | sed "s/.*<version>\([^<]*\)<\/version>.*/\1/")
build=$(curl -s $path/$version/maven-metadata.xml | grep '<value>' | head -1 | sed "s/.*<value>\([^<]*\)<\/value>.*/\1/")
file=$artifactId-$build.$type
url=$path/$version/$file

# Download
echo $url
wget -O ${IVY_HOME}/deploy/app.zip -q -N $url