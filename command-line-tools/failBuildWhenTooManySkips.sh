#!/bin/bash

SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );

# first argument = path to testng-results.xml file [optional]
failWhenTooManySkips(){
  # check if value in $1 points to an existing regular file, if so, it is set as path to file; otherwise find is used starting from qa repo
  if [ -f "$1" ];then
     XML_FILE=$1;
  else
     # this will locate the testng xml file based on script directory - workaround for solaris where path could not be found
     XML_FILE=$(find $( dirname ${SCRIPT_DIR}) -name testng-results.xml);
  fi

  if [ ! -f "${XML_FILE}" ];then
     echo "File '${XML_FILE}' does not exist. Exiting...";
     exit 1;
  fi

  MAX_SKIPS_FOR_SUCCESS=2;

  SKIPS=`head -2 ${XML_FILE} | tail -1 | sed -n 's/.* skipped="\([^"]\).*/\1/p'`;
  echo "Found: ${SKIPS} skips";
  if [ ${SKIPS} -gt ${MAX_SKIPS_FOR_SUCCESS} ] ;then
     echo "Too many skips, FAILING the build";
     return ${SKIPS};
  else
     echo "Found fewer skips than threshold. The build is not corrupted."
     exit 0;
  fi
}

failWhenTooManySkips $1 $2
