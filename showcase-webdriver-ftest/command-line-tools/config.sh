#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;

# Device properties
DEVICE_MEMORY=500M;
DEVICE_NAME_DEFAULT=showcase-ftest-device;
DEVICE_SERIAL=emulator-5554;
DEVICE_VERSION_DEFAULT=8;

# WebDriver properties
WEBDRIVER_FILE_PREFIX=android-server-;
WEBDRIVER_FILE_SUFFIX=.apk;
WEBDRIVER_FILE_VERSION_DEFAULT=2.6.0;
WEBDRIVER_DEVICE_PORT_DEFAULT=8080;
WEBDRIVER_HOST_PORT_DEFAULT=4444;
WEBDRIVER_DOWNLOAD_URL=http://selenium.googlecode.com/files;

# Android SDK properties
ANDROID_ARCHIVE=android-sdk_r12-linux_x86.tgz;
ANDROID_DOWNLOAD_URL=http://dl.google.com/android;

# Workspace
WORKSPACE_DEFAULT=$SCRIPT_DIR/android-workspace;

# Functions

function msg()
{
	echo "### $1";
}

usage()
{
cat << EOF
usage: $0 options

This script runs android emulator

OPTIONS:
	-d 	device port (defaultly set to 8080)
	-h	host port (defaultly set to 4444)
	-i	unable installing android server to the device
	-l 	skip starting a new device
	-n	device name
	-p	workspace
	-s	android SDK directory (defaultly set to 2.9.0)
	-v 	device verison (defaultly set to 11)
	-w	android selenium server version
EOF
}

# Argument loading

ARG_ANDROID_SDK=;
ARG_DEVICE_NAME=$DEVICE_NAME_DEFAULT;
ARG_DEVICE_PORT=$WEBDRIVER_DEVICE_PORT_DEFAULT;
ARG_DEVICE_VERSION=$DEVICE_VERSION_DEFAULT;
ARG_INSTALL_WEBDRIVER=1;
ARG_HOST_PORT=$WEBDRIVER_HOST_PORT_DEFAULT;
ARG_START_DEVICE=1;
ARG_WEBDRIVER_FILE_VERSION=$WEBDRIVER_FILE_VERSION_DEFAULT;
ARG_WORKSPACE=$WORKSPACE_DEFAULT;

while getopts "d:h:iln:p:s:v:w:" OPTION
do
	case $OPTION in
		d)
			ARG_DEVICE_PORT=$OPTARG;
			msg "device port has been set to [$OPTARG]";
			;;
		h)
			ARG_HOST_PORT=$OPTARG;
			msg "host port has been set to [$OPTARG]";
			;;
		i)
			ARG_INSTALL_WEBDRIVER=0;
			msg "skipping installation of android selenium server";
			;;
		l)
			ARG_START_DEVICE=0;
			msg "skipping starting device";
			;;
		n)
			ARG_DEVICE_NAME=$OPTARG;
			msg "device name has been set to [$OPTARG]";
			;;
		p)
			ARG_WORKSPACE=$OPTARG;
			msg "workspace has been set to [$OPTARG]";
			;;
		s)
			ARG_ANDROID_SDK=$OPTARG;
			msg "android sdk directory has been set to [$OPTARG]";
			;;		
		v)
			ARG_DEVICE_VERSION=$OPTARG;
			msg "device version has been set to [$OPTARG]";
			;;
		w)
			ARG_WEBDRIVER_FILE_VERSION=$OPTARG;
			msg "android selenium server version has been set to [$OPTARG]";
			;;
		?)
			usage;
			exit 1;
			;;
	esac
done

# Argument validation

if [ ! -d "$ARG_ANDROID_SDK" ]; then
	msg "set android sdk [$ARG_ANDROID_SDK] is not a directory";
	usage;
	exit 1;
fi

if [ ! -d "$ARG_WORKSPACE" ]; then
	msg "creating workspace [$ARG_WORKSPACE]";
	mkdir -p "$ARG_WORKSPACE";
fi
