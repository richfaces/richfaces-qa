#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

if [ "$RICHFACES_VERSION"  == "" ]; then
    RICHFACES_VERSION=`grep version.richfaces ${SCRIPT_DIR}/../../pom.xml | head -n 1 | tr '<>' ' ' | awk '{ print $2; }'`
fi

