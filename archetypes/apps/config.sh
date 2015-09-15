#!/bin/bash
if [ "$MAVEN"  == "" ]; then
    if [ -f "/qa/tools/opt/apache-maven-3.2.5/bin/mvn" ]; then
	    MAVEN="/qa/tools/opt/apache-maven-3.2.5/bin/mvn"
    elif [ -f "t:/opt/apache-maven-3.2.5/bin/mvn" ]; then
	    MAVEN="t:/opt/apache-maven-3.2.5/bin/mvn"
    else
        MAVEN="mvn"
    fi
fi

echo "--------------------------------------------------------------------------------";
echo "Maven binary: ${MAVEN}" 
echo "Maven version:"
${MAVEN} -v;
echo "--------------------------------------------------------------------------------";

MAVEN_ARGS="-V -U"
