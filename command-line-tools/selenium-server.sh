#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

VERSION=`grep version.selenium.server ${SCRIPT_DIR}/../pom.xml | head -n 1 | tr '<>' ' ' | awk '{ print $2; }'`
JAR="${SCRIPT_DIR}/selenium-server-standalone-$VERSION.jar"
LOG="${SCRIPT_DIR}/target/selenium/selenium-server.log"
PORT=14444

mkdir -p `dirname $LOG` || true

if [ ! -f "$JAR" ]; then
	wget http://selenium.googlecode.com/files/selenium-server-standalone-$VERSION.jar -O $JAR
fi

java -jar $JAR -log $LOG -port $PORT -browserSessionReuse $*
