#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
source "$SCRIPT_DIR/version.sh";

mvn archetype:generate -U -DarchetypeGroupId=org.richfaces.archetypes -DarchetypeArtifactId=richfaces-archetype-simpleapp -DarchetypeVersion=${VERSION} -DgroupId=org.richfaces.tests.archetypes -DartifactId=richfaces-simpleapp -Dversion=${VERSION} -Dpackage=org.richfaces.tests.archetypes.simpleapp -DinteractiveMode=false -DarchetypeRepository=https://repository.jboss.org/nexus/content/groups/public/;
