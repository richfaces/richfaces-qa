#!/bin/bash
read -p 'Enter release version: ' RELEASE
read -p 'Enter new development version: ' DEVELOPMENT
read -p 'Enter path to the settings.xml: ' MVN_SETTINGS

CONF="--batch-mode -Dtag=richfaces-simpleapp-ftest-${RELEASE} -DreleaseVersion=${RELEASE} -DdevelopmentVersion=${DEVELOPMENT}"

if [ -n "$MVN_SETTINGS" ]; then
   CONF="$CONF -s $MVN_SETTINGS"; 
fi

echo "Configuration: ${CONF}"

read -p 'Press ENTER to clean...'
mvn release:clean clean ${CONF}
read -p 'Press ENTER to dry run...'
mvn release:prepare -DdryRun=true ${CONF} || exit 1
read -p 'Press ENTER to clean...'
mvn release:clean ${CONF}
read -p 'Press ENTER to prepare...'
mvn clean release:prepare ${CONF} || exit 1
