#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
source "$SCRIPT_DIR/version.sh";

if [ -d "$SCRIPT_DIR/richfaces-simpleapp" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-simpleapp";
fi
cd $SCRIPT_DIR;
mvn archetype:generate -U -DarchetypeGroupId=org.richfaces.archetypes -DarchetypeArtifactId=richfaces-archetype-simpleapp -DarchetypeVersion=${VERSION} -DgroupId=org.richfaces.tests.archetypes -DartifactId=richfaces-simpleapp -Dversion=${VERSION} -Dpackage=org.richfaces.tests.archetypes.simpleapp -DinteractiveMode=false $@;
cd richfaces-simpleapp;
mvn clean package -Prelease;
cd ..;
