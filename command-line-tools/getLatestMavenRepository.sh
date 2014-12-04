#!/bin/bash
# use sh getLatestMavenRepository.sh ; to download repository from latest snapshots
# use sh getLatestMavenRepository.sh release ; to download repository from latest release

SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );

# add additional functions
# 'source' is not working on Solaris, using '.' instead.
. ${SCRIPT_DIR}/extract.sh;
. ${SCRIPT_DIR}/download.sh;

getLatestMavenRepository(){
  URL_SNAPSHOTS=http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/4.5/job/richfaces-4.5-metamer-repositories-packer/lastSuccessfulBuild/artifact/maven-repository.zip;
  URL_RELEASE=http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/Release/job/richfaces-4.5-release-metamer-repositories-packer/lastSuccessfulBuild/artifact/maven-repository.zip;
  URL_WFK=http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/RF-Prod/job/richfaces-wfk-2.7-tests/lastSuccessfulBuild/artifact/maven-repository.zip;

  if [ "$1" == "release" ] ;then
     URL=${URL_RELEASE};
  elif [ "$1" == "wfk" ] ;then
     URL=${URL_WFK};
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