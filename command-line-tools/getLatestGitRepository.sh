#!/bin/bash
# use sh getLatestGitRepository.sh ; to download repository from latest snapshots
# use sh getLatestGitRepository.sh githubBranch; to download repository from latest snapshots and to switch to a specific branch/tag

SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );

# add additional functions
# 'source' is not working on Solaris, using '.' instead.
. ${SCRIPT_DIR}/extract.sh;
. ${SCRIPT_DIR}/download.sh;

getLatestQAGitRepository(){
  URL_SNAPSHOTS=http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/4.5/job/richfaces-4.5-metamer-repositories-packer/lastSuccessfulBuild/artifact/git-repository.zip;

  download ${URL_SNAPSHOTS}

  if [ $? -gt 0 ];then
    echo "downloading was unsuccessful. Exiting."
    exit 1;
  fi

  extract git-repository.zip

  cd qa
  checkoutToBranchAndUpdate $1
}

checkoutToBranchAndUpdate(){
  if [ ! -z "$1" ];then
    BRANCH=$1;
  else
    BRANCH=master
  fi

#  echo "BRANCH=${BRANCH}"

  git stash save
  git checkout master
  git pull
  if [[ ! "$BRANCH" == "master"  ]];then
    git checkout $BRANCH
  fi
}

getLatestQAGitRepository $1