#!/bin/bash

download(){
  if [ ! -z "$1" ];then
     URL=$1;
  else
     exit 1;
  fi
  wget ${URL} --no-check-certificate -c
  if [ "$?" -ne "0" ];then
     # Mac workaround
     echo "Trying to download the file without option '--no-check-certificate'"
     wget ${URL} -c
  fi
}
