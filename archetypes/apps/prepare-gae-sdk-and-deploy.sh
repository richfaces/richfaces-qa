#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

if [ ! -d "$SCRIPT_DIR/appengine-java-sdk" ]; then
    wget http://googleappengine.googlecode.com/files/appengine-java-sdk-1.6.1.zip -O $SCRIPT_DIR/appengine-java-sdk.zip;
    unzip $SCRIPT_DIR/appengine-java-sdk.zip -d $SCRIPT_DIR;
    rm -rf $SCRIPT_DIR/appengine-java-sdk.zip;
    mv $SCRIPT_DIR/appengine-java-sdk-1.6.1 $SCRIPT_DIR/appengine-java-sdk; 
fi

echo "$2" | $SCRIPT_DIR/appengine-java-sdk/bin/appcfg.sh  -e $1 update $SCRIPT_DIR/richfaces-gae/target/richfaces-gae;
