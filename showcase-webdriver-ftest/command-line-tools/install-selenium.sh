#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

source "$SCRIPT_DIR/config.sh";

ARG_ADB=$1;
ARG_DEVICE_SERIAL=$2;
ARG_DEVICE_PORT=$3;
ARG_HOST_PORT=$4;
ARG_SELENIUM_VERSION=$5;

ARG_WORKSPACE=$WORKSPACE_DEFAULT;

if [ ! -d "$ARG_WORKSPACE" ]; then
	msg "creating workspace [$ARG_WORKSPACE]";
	mkdir -p "$ARG_WORKSPACE";
fi

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
if [ ! -e $ARG_WORKSPACE/$WEBDRIVER ]; then
	msg "downloading webdriver to [$ARG_WORKSPACE/$WEBDRIVER]";
	wget $WEBDRIVER_DOWNLOAD_URL/$WEBDRIVER --output-document $ARG_WORKSPACE/$WEBDRIVER;
fi	

msg "installing web driver [$WEBDRIVER]";
for ((I=0; I<$TIMOUT_TRIES; I++)); do	
    msg "... try $I";
    GREPPED=`$ARG_ADB -s $ARG_DEVICE_SERIAL -e install -r $ARG_WORKSPACE/$WEBDRIVER | grep Error`;
    if [ "$GREPPED" == "" ]; then
        INSTALLED=1;
        msg "... success";
        break;
    else
        msg "... failed";
    fi
    sleep $SLEEP_AMOUNT;
done

if [ $INSTALLED == "" ]; then
    msg "installation of web driver failed";
    exit 1;
fi

msg "forwarding tcp: host port $ARG_HOST_PORT -> device port $ARG_DEVICE_PORT";
$ARG_ADB -s $ARG_DEVICE_SERIAL forward tcp:$ARG_HOST_PORT tcp:$ARG_DEVICE_PORT;
