#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;

source "$SCRIPT_DIR/config.sh";

ARG_ADB=$1;
ARG_DEVICE_SERIAL=$2;
ARG_DEVICE_PORT=$3;
ARG_HOST_PORT=$4;
ARG_SELENIUM_VERSION=$5;

WORKSPACE=/tmp

if [ ! ${ARG_ADB} ]; then
	echo "The argument [1] is missining";
	exit 1;
fi

if [ ! ${ARG_DEVICE_SERIAL} ]; then
	echo "The argument [2] is missining";
	exit 1;
fi

if [ ! ${ARG_DEVICE_PORT} ]; then
	echo "The argument [3] is missining";
	exit 1;
fi

if [ ! ${ARG_HOST_PORT} ]; then
	echo "The argument [4] is missining";
	exit 1;
fi

if [ ! ${ARG_SELENIUM_VERSION} ]; then
	echo "The argument [5] is missining";
	exit 1;
fi

WEBDRIVER=${WEBDRIVER_FILE_PREFIX}${ARG_SELENIUM_VERSION}${WEBDRIVER_FILE_SUFFIX};
if [ ! -e $WORKSPACE/$WEBDRIVER ]; then
	msg "downloading webdriver to [$ARG_WORKSPACE/$WEBDRIVER]";
	wget $WEBDRIVER_DOWNLOAD_URL/$WEBDRIVER --output-document $WORKSPACE/$WEBDRIVER;
fi	
msg "installing web driver [$WEBDRIVER]";	
$ARG_ADB -s $DEVICE_SERIAL -e install -r $WORKSPACE/$WEBDRIVER;
msg "forwarding tcp: host port $ARG_HOST_PORT -> device port $ARG_DEVICE_PORT";
$ARG_ADB -s $DEVICE_SERIAL forward tcp:$ARG_HOST_PORT tcp:$ARG_DEVICE_PORT;
