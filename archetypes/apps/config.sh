#!/bin/bash
if [ "$MAVEN"  == "" ]; then
	if [ -f "/qa/tools/opt/apache-maven-3.0.4/bin/mvn" ]; then
		MAVEN="/qa/tools/opt/apache-maven-3.0.4/bin/mvn"
	else
		MAVEN="mvn"
	fi
fi

echo "--------------------------------------------------------------------------------";
echo "Maven binary: ${MAVEN}" 
echo "Maven version:"
${MAVEN} -v;
echo "--------------------------------------------------------------------------------";

MAVEN_ARGS="--show-version -U"
