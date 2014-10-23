#!/bin/bash

download(){
  if [ ! -z "$1" ];then
     URL=$1;
  else
     exit 1;
  fi
  wget ${URL} --no-check-certificate -c
}
