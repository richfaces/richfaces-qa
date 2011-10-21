#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;

# Device properties
DEVICE_MEMORY=500M;
DEVICE_NAME_DEFAULT=metamer;
DEVICE_SERIAL=emulator-5554;
DEVICE_VERSION_DEFAULT=10;

# WebDriver properties
WEBDRIVER_FILE_PREFIX=android-server-;
WEBDRIVER_FILE_SUFFIX=.apk;
WEBDRIVER_FILE_VERSION_DEFAULT=2.9.0;
WEBDRIVER_DEVICE_PORT_DEFAULT=8080;
WEBDRIVER_HOST_PORT_DEFAULT=4444;
WEBDRIVER_DOWNLOAD_URL=http://selenium.googlecode.com/files;

# Android SDK properties
ANDROID_ARCHIVE=android-sdk_r12-linux_x86.tgz;
ANDROID_DOWNLOAD_URL=http://dl.google.com/android;

# Workspace
WORKSPACE_DEFAULT=$SCRIPT_DIR/android-workspace;

# Argument loading

ARG_ANDROID_SDK=;
ARG_DEVICE_NAME=$DEVICE_NAME_DEFAULT;
ARG_DEVICE_PORT=$WEBDRIVER_DEVICE_PORT_DEFAULT;
ARG_DEVICE_VERSION=$DEVICE_VERSION_DEFAULT;
ARG_INSTALL_WEBDRIVER=1;
ARG_HOST_PORT=$WEBDRIVER_HOST_PORT_DEFAULT;
ARG_WEBDRIVER_FILE_VERSION=$WEBDRIVER_FILE_VERSION_DEFAULT;
ARG_WORKSPACE=$WORKSPACE_DEFAULT;

usage()
{
cat << EOF
usage: $0 options

This script runs android emulator

OPTIONS:
	-d 	device port (defaultly set to 8080)
	-h	host port (defaultly set to 4444)
	-n	device name
	-p	workspace
	-s	android SDK directory (defaultly set to 2.9.0)
	-u	unable installing android server to the device
	-v 	device verison (defaultly set to 11)
	-w	android selenium server version
EOF
}

while getopts "d:h:s:v:w:" OPTION
do
	case $OPTION in
		d)
			ARG_DEVICE_PORT=$OPTARG;
			;;
		h)
			ARG_HOST_PORT=$OPTARG;
			;;
		n)
			ARG_DEVICE_NAME=$OPTARG;
			;;
		p)
			ARG_WORKSPACE=$OPTARG;
			;;
		s)
			ARG_ANDROID_SDK=$OPTARG;
			;;	
		u)
			ARG_INSTALL_WEBDRIVER=0;
			;;	
		v)
			ARG_DEVICE_VERSION=$OPTARG;
			;;
		w)
			ARG_WEBDRIVER_VERSION=$OPTARG;
			;;
		?)
			usage;
			exit 1;
			;;
	esac
done

# Argument validation

function msg()
{
	echo "### $1";
}

if [ ! -d "$ARG_ANDROID_SDK" ]; then
	msg "set android sdk [$ARG_ANDROID_SDK] is not a directory";
	usage;
	exit 1;
fi

if [ ! -d "$ARG_WORKSPACE" ]; then
	msg "creating workspace [$ARG_WORKSPACE]";
	mkdir "$ARG_WORKSPACE";
fi
