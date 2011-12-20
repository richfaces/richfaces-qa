#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -m $SCRIPT_DIR`;

source "$SCRIPT_DIR/version.sh";
source "$SCRIPT_DIR/config.sh";

if [ -d "$SCRIPT_DIR/richfaces-simpleapp" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-simpleapp";
fi

cd $SCRIPT_DIR;
mvn ${MAVEN_ARGS} archetype:generate -DarchetypeGroupId=org.richfaces.archetypes -DarchetypeArtifactId=richfaces-archetype-simpleapp -DarchetypeVersion=${RICHFACES_VERSION} -DgroupId=org.richfaces.tests.archetypes -DartifactId=richfaces-simpleapp -Dversion=${RICHFACES_VERSION} -Dpackage=org.richfaces.tests.archetypes.simpleapp -DinteractiveMode=false $@;
cd richfaces-simpleapp;
${MAVEN} ${MAVEN_ARGS} -f $SCRIPT_DIR/richfaces-gae/pom.xml clean package -Prelease;
cd ..;
