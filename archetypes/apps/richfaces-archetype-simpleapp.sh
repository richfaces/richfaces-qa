#!/bin/bash
SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );
WORKING_DIR=`pwd`;
# add additional functions/variables
# 'source' is not working on Solaris, using '.' instead.
. $SCRIPT_DIR/version.sh;
. $SCRIPT_DIR/config.sh;

if [ -d "$SCRIPT_DIR/richfaces-simpleapp" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-simpleapp";
fi

echo "--------------------------------------------------------------------------------";
echo "RichFaces version: ${RICHFACES_VERSION}" 
echo "--------------------------------------------------------------------------------";

cd $SCRIPT_DIR;
${MAVEN} ${MAVEN_ARGS} -U archetype:generate -DarchetypeGroupId=org.richfaces.archetypes -DarchetypeArtifactId=richfaces-archetype-simpleapp -DarchetypeVersion=${RICHFACES_VERSION} -DgroupId=org.richfaces.tests.archetypes -DartifactId=richfaces-simpleapp -Dversion=${RICHFACES_VERSION} -Dpackage=org.richfaces.tests.archetypes.simpleapp -DinteractiveMode=false $@;
${MAVEN} ${MAVEN_ARGS} -U -f richfaces-simpleapp/pom.xml clean package -Prelease $@;
cd $WORKING_DIR;
