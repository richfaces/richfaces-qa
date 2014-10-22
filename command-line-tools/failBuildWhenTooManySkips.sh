#!/bin/bash

SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

# first argument = path to testng-results.xml file [optional], default '../metamer/ftest/target/surefire-reports/testng-results.xml'
# second argument = threshold number of skips after which the build will fail [optional], default '2'
failWhenTooManySkips(){
  if [[ ! -z "$1" ]];then
     XML_FILE=$1;
  else
     XML_FILE="../metamer/ftest/target/surefire-reports/testng-results.xml";
  fi

#  echo "XML_FILE=${XML_FILE}"

  cd ${SCRIPT_DIR}

  if [ ! -f ${XML_FILE} ];then
     echo "No such file ${XML_FILE}. Exiting...";
     return 1;
  fi

  if [ ! -z "$2" ] && [ "$2" -gt 0 ] ;then
        MAX_SKIPS_FOR_SUCCESS=$2;
     else
        MAX_SKIPS_FOR_SUCCESS=2;
  fi

#  echo "MAX_SKIPS_FOR_SUCCESS=${MAX_SKIPS_FOR_SUCCESS}"


  SKIPS=`head ${XML_FILE} -n 2 | tail -n 1 | sed -n 's/.* skipped="\([^"]\+\).*/\1/p'`
  echo "Found: ${SKIPS} skips";
  if [ ${SKIPS} -gt ${MAX_SKIPS_FOR_SUCCESS} ]; then
     echo "Too many skips, FAILING the build";
     return ${SKIPS};
  else
     return 0;
  fi
}

failWhenTooManySkips $1 $2