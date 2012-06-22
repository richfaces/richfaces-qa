#!/bin/bash
mvn clean install -Prelease -Dmaven.test.skip=true $*
