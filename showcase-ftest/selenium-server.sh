#!/bin/bash

SERVER="src/test/resources/selenium-server-standalone.jar"
LOG="target/selenium/selenium-server.log"
PORT=8444

mkdir -p `dirname $LOG` || true

if [ ! -f "$SERVER" ]; then
	mvn process-resources -Dselenium.server.skip=true -Dselenium.test.skip=true -Pprovide-selenium-server-standalone || exit 1
fi

java -jar $SERVER -log $LOG -port $PORT -browserSessionReuse $*
