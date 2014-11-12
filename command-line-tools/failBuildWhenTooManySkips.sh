#!/bin/bash

#replacement for readlink command which is not available on Solaris
canonicalpath() {
  if [ -d "$1" ]; then
    pushd $1 > /dev/null 2>&1
    echo $PWD
  elif [ -f "$1" ]; then
    pushd $(dirname $1) > /dev/null 2>&1
    echo $PWD/$(basename $1)
  else
    echo "Invalid path $1"
  fi
  popd > /dev/null 2>&1
}

SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`canonicalpath $SCRIPT_DIR`;

# first argument = path to testng-results.xml file [optional], default '../metamer/ftest/target/surefire-reports/testng-results.xml'
# second argument = threshold number of skips after which the build will fail [optional], default '2'
failWhenTooManySkips(){
  # check if value in $1 points to an existing regular file, if so, it is set as path to file; otherwise find is used starting from qa repo
  if [ -f "$1" ];then
     XML_FILE=$1;
  else
     # this will locate the testng xml file based on script directory - workaround for solaris where path could not be found
     XML_FILE=$(find $( dirname ${SCRIPT_DIR}) -name testng-results.xml);
  fi

  if [ ! -f "${XML_FILE}" ];then
     echo "No such file ${XML_FILE}. Exiting...";
     return 1;
  fi

  if [ ! -z "$2" ] && [ "$2" -gt 0 ] ;then
        MAX_SKIPS_FOR_SUCCESS=$2;
     else
        MAX_SKIPS_FOR_SUCCESS=2;
  fi

#  echo "MAX_SKIPS_FOR_SUCCESS=${MAX_SKIPS_FOR_SUCCESS}"


  SKIPS=`head -2 ${XML_FILE} | tail -1 | sed -n 's/.* skipped="\([^"]\+\).*/\1/p'`;
  echo "Found: ${SKIPS} skips";
  if [ ${SKIPS} -gt ${MAX_SKIPS_FOR_SUCCESS} ]; then
     echo "Too many skips, FAILING the build";
     return ${SKIPS};
  else
     return 0;
  fi
}

failWhenTooManySkips $1 $2
