#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;

source "$SCRIPT_DIR/config.sh";

# Functions

usage()
{
cat << EOF
usage: $0 options

This script runs android emulator

OPTIONS:
	-a	adb
	-d	device port
	-h 	host port
	-n	device name
	-v	selenium version
EOF
}

# Arguments
ARG_ADB="adb";
ARG_DEVICE_NAME=$ANDROID_AVD_DEVICE;
ARG_DEVICE_PORT=$WEBDRIVER_DEVICE_PORT_DEFAULT;
ARG_HOST_PORT=$WEBDRIVER_HOST_PORT_DEFAULT;
ARG_WEBDRIVER_FILE_VERSION=$WEBDRIVER_FILE_VERSION_DEFAULT;

while getopts "a:d:h:n:v:" OPTION
do
	case $OPTION in
		a)
			ARG_ADB=$OPTARG;
			;;
		d)
			ARG_DEVICE_PORT=$OPTARG;
			;;
		h)
			ARG_HOST_PORT=$OPTARG;
			;;
		n)
			ARG_DEVICE_NAME=$OPTARG;
			;;
		v)
			ARG_WEBDRIVER_FILE_VERSION=$OPTARG;
			;;
		?)
			usage;
			exit 1;
			;;
	esac
done

# Executing
msg "executing"
msg "${SCRIPT_DIR}/install-selenium.sh $ARG_ADB $ARG_DEVICE_NAME $ARG_DEVICE_PORT $ARG_HOST_PORT $ARG_WEBDRIVER_FILE_VERSION";
${SCRIPT_DIR}/install-selenium.sh "$ARG_ADB" $ARG_DEVICE_NAME $ARG_DEVICE_PORT $ARG_HOST_PORT $ARG_WEBDRIVER_FILE_VERSION;
msg "${SCRIPT_DIR}/start-selenium.sh $ARG_ADB";
${SCRIPT_DIR}/start-selenium.sh "$ARG_ADB"
