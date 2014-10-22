#!/bin/bash

getLatestQAGitRepository(){
  echo "downloading latest QA git repository"
  wget http://jenkins.mw.lab.eng.bos.redhat.com/hudson/view/RichFaces/view/4.5/job/richfaces-4.5-metamer-repositories-packer/lastSuccessfulBuild/artifact/git-repository.zip --no-check-certificate -nv
  echo "downloading of QA git repository completed"

  if [ $? -gt 0 ];then
    return $?
  fi

  echo "extracting git-repository.zip"
  jar xf git-repository.zip
  echo "extracting of git-repository.zip completed"

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