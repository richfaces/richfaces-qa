#!/bin/bash

JAR="selenium-server-standalone.jar"
LOG="target/selenium/selenium-server.log"
PORT=8444

mkdir -p `dirname $LOG` || true

if [ ! -f "$JAR" ]; then
	VERSION=`grep version.selenium.server ../pom.xml | head -n 1 | tr '<>' ' ' | awk '{ print $2; }'`
	wget http://selenium.googlecode.com/files/selenium-server-standalone-$VERSION.jar -O $JAR
fi

java -jar $JAR -log $LOG -port $PORT -browserSessionReuse $*
