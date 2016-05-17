#!/bin/bash
SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );

if [ "$RICHFACES_VERSION"  == "" ]; then
    RICHFACES_VERSION=`grep version.richfaces ${SCRIPT_DIR}/../../pom.xml | head -n 1 | tr '<>' ' ' | awk '{ print $2; }'`
fi

