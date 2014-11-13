#!/bin/bash
mvn clean install -f application/pom.xml -Prelease -Dmaven.test.skip=true $*
