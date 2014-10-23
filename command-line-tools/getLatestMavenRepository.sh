#!/bin/bash
# use sh getLatestMavenRepository.sh ; to download repository from latest snapshots
# use sh getLatestMavenRepository.sh release ; to download repository from latest release

SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

source ${SCRIPT_DIR}/extract.sh
source ${SCRIPT_DIR}/download.sh

getLatestMavenRepository(){
  URL_SNAPSHOTS=http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/4.5/job/richfaces-4.5-metamer-repositories-packer/lastSuccessfulBuild/artifact/maven-repository.zip;
  URL_RELEASE=http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/Release/job/richfaces-4.5-release-metamer-repositories-packer/lastSuccessfulBuild/artifact/maven-repository.zip;

  if [ ! -z "$1" ] && [ "$1" == "release" ] ;then
     URL=${URL_RELEASE};
  else
     URL=${URL_SNAPSHOTS};
  fi

  download ${URL}

  if [ $? -gt 0 ];then
    echo "downloading was unsuccessful. Exiting."
    exit 1;
  fi

  extract maven-repository.zip
}

getLatestMavenRepository $1