#!/bin/bash
SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );
WORKING_DIR=`pwd`;
# add additional functions/variables
# 'source' is not working on Solaris, using '.' instead.
. $SCRIPT_DIR/version.sh;
. $SCRIPT_DIR/config.sh;

if [ -d "$SCRIPT_DIR/richfaces-kitchensink" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-kitchensink";
fi

echo "--------------------------------------------------------------------------------";
echo "RichFaces version: ${RICHFACES_VERSION}" 
echo "--------------------------------------------------------------------------------";

cd $SCRIPT_DIR;
${MAVEN} ${MAVEN_ARGS} -U archetype:generate -DarchetypeGroupId=org.richfaces.archetypes -DarchetypeArtifactId=richfaces-archetype-kitchensink -DarchetypeVersion=${RICHFACES_VERSION} -DgroupId=org.richfaces.tests.archetypes -DartifactId=richfaces-kitchensink -Dversion=${RICHFACES_VERSION} -Dpackage=org.richfaces.tests.archetypes.kitchensink -DinteractiveMode=false $@;
${MAVEN} ${MAVEN_ARGS} -U -f richfaces-kitchensink/pom.xml clean package $@;
cd $WORKING_DIR;
