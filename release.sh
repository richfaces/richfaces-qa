#!/bin/bash
RICHFACES_VERSION=`grep '<version.richfaces>' pom.xml | sed -r 's#.*>([^<]+)<.*#\1#'`

echo "Project defines following version of RichFaces: ${RICHFACES_VERSION}"
echo $RICHFACES_VERSION | egrep -q '\-SNAPSHOT$' && { echo "The project cannot depend on SNAPSHOT version, correct the version first and try again."; exit 2; }
#perl -e 'open (FILE, "<", "pom.xml"); local $/; $file=<FILE>; close (FILE); if ($file !~ m#\>'$RICHFACES_VERSION'\<\/version\>\s+\<\/parent\>#sg) { exit 1 }' || { echo "The project parent have to be same version like RichFaces version"; exit 3; }
echo "Is this version correct?"
read -p 'Press ENTER to continue or Ctrl+C for exit...'
read -p 'Enter release version: ' RELEASE
read -p 'Enter new development version: ' DEVELOPMENT
read -p 'Enter path to the settings.xml: ' MVN_SETTINGS

CONF="--batch-mode -Dtag=${RELEASE} -DreleaseVersion=${RELEASE} -DdevelopmentVersion=${DEVELOPMENT} -DignoreSnapshots=true"

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
read -p 'Press ENTER to perform...'
mvn release:perform ${CONF} || exit 1
