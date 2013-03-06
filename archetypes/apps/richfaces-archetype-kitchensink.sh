#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;
WORKING_DIR=`pwd`;
source "$SCRIPT_DIR/version.sh";
source "$SCRIPT_DIR/config.sh";

if [ -d "$SCRIPT_DIR/richfaces-kitchensink" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-kitchensink";
fi

echo "--------------------------------------------------------------------------------";
echo "RichFaces version: ${RICHFACES_VERSION}" 
echo "--------------------------------------------------------------------------------";

cd $SCRIPT_DIR;
${MAVEN} ${MAVEN_ARGS} -U archetype:generate -Dbasedir=${SCRIPT_DIR} -DarchetypeGroupId=org.richfaces.archetypes -DarchetypeArtifactId=richfaces-archetype-kitchensink -DarchetypeVersion=${RICHFACES_VERSION} -DgroupId=org.richfaces.tests.archetypes -DartifactId=richfaces-kitchensink -Dversion=${RICHFACES_VERSION} -Dpackage=org.richfaces.tests.archetypes.kitchensink -DinteractiveMode=false $@;
${MAVEN} ${MAVEN_ARGS} -U -f $SCRIPT_DIR/richfaces-kitchensink/pom.xml clean package $@;
cd $WORKING_DIR;
