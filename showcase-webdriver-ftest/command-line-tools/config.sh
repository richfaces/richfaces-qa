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

# Other
SLEEP_AMOUNT=5
TIMOUT_TRIES=10

# Functions

function msg()
{
	echo "### $1";
}
