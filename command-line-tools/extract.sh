#!/bin/bash

extract(){
  if [ ! -z "$1" ];then
     FILE=$1;
  else
     exit 1;
  fi

  echo "extracting file ${FILE}"
  jar xf ${FILE}
  echo "extracting of file ${FILE} completed"
}