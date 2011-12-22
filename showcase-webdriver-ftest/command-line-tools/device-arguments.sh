#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

source "$SCRIPT_DIR/config.sh";

# Functions

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
	-s	android SDK directory 
	-v 	device verison (defaultly set to 11)
	-w	android selenium server version (defaultly set to 2.9.0)
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

# Info messages

msg "workspace is set to [$ARG_WORKSPACE]";

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
