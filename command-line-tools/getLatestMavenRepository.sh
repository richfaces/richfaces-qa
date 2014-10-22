#!/bin/bash
cd $WORKSPACE

echo "downloading latest maven repository for testing in Metamer"
wget http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/4.5/job/richfaces-4.5-metamer-repositories-packer/lastSuccessfulBuild/artifact/maven-repository.zip --no-check-certificate -nv

echo "extracting maven-repository.zip"
jar xf maven-repository.zip
echo "extracting of maven-repository.zip completed"