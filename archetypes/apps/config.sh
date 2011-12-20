#!/bin/bash

if [ -f "/qa/tools/opt/apache-maven-3.0.3/bin/mvn" ]; then
    MAVEN="/qa/tools/opt/apache-maven-3.0.3/bin/mvn"
else
    MAVEN="mvn"
fi
