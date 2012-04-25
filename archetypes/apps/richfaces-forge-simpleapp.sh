#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;
WORKING_DIR=`pwd`;
source "$SCRIPT_DIR/version.sh";
source "$SCRIPT_DIR/config.sh";

if [ -d "$SCRIPT_DIR/richfaces-simpleapp" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-simpleapp";
fi

if [ "$FORGE_VERSION"  == "" ]; then
    FORGE_VERSION="1.0.3.Final";
fi

if [ -d "$SCRIPT_DIR/forge-distribution-${FORGE_VERSION}" ]; then
    rm -rf "$SCRIPT_DIR/forge-distribution-${FORGE_VERSION}";
fi;

if [ ! -f "$SCRIPT_DIR/forge-distribution-${FORGE_VERSION}.zip" ]; then
    wget "https://repository.jboss.org/nexus/service/local/artifact/maven/redirect?r=releases&g=org.jboss.forge&a=forge-distribution&v=${FORGE_VERSION}&e=zip" -O "$SCRIPT_DIR/forge-distribution-${FORGE_VERSION}.zip" --no-check-certificate;
fi

unzip "$SCRIPT_DIR/forge-distribution-${FORGE_VERSION}.zip";

if [ -d "~/.forge" ]; then
    rm -rf "~/.forge";
fi

if [ -d "$SCRIPT_DIR/richfaces-forge-simpleapp" ]; then
    rm -rf "$SCRIPT_DIR/richfaces-forge-simpleapp";
fi

echo "--------------------------------------------------------------------------------";
echo "RichFaces version: ${RICHFACES_VERSION}" 
echo "--------------------------------------------------------------------------------";

cd $SCRIPT_DIR;
    for I in {1..2}; do echo ""; done | $SCRIPT_DIR/forge-distribution-${FORGE_VERSION}/bin/forge -e "new-project --named richfaces-forge-simpleapp --topLevelPackage org.richfaces.tests --projectFolder richfaces-forge-simpleapp; richfaces setup; richfaces install-example-facelet; build;"
cd $WORKING_DIR;
