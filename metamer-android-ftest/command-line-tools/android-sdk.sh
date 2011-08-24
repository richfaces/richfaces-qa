 #!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
ANDROID_SDK=android-sdk-linux_x86;
ANDROID_DOWNLOAD_URL=http://dl.google.com/android;
ANDROID_ARCHIVE=android-sdk_r12-linux_x86.tgz;

WEBDRIVER_DOWNLOAD_URL=http://selenium.googlecode.com/files;
WEBDRIVER=android-server-2.0rc3-patched-for-tablets.apk;

WORKSPACE_DEFAULT=$SCRIPT_DIR/../android;

echo " *** setting android sdk";

if [ -n "$1" ]
then
        WORKSPACE=$1;
else
	WORKSPACE=$WORKSPACE_DEFAULT;
fi
echo " *** workspace set to [$WORKSPACE]";

if [ ! -d "$1" ]
then
	mkdir $WORKSPACE;
fi

if [ ! -e $WORKSPACE/$ANDROID_SDK ];
then
	if [ ! -e $WORKSPACE/$ANDROID_ARCHIVE ];
	then
		echo " *** downloading android sdk from [$ANDROID_DOWNLOAD_URL/$ANDROID_ARCHIVE]";
		wget $ANDROID_DOWNLOAD_URL/$ANDROID_ARCHIVE --output-document $WORKSPACE/$ANDROID_ARCHIVE;
	fi
	echo " *** unpacking [$ANDROID_ARCHIVE] to [$ANDROID_SDK]";
	tar -xf $WORKSPACE/$ANDROID_ARCHIVE --directory=$WORKSPACE;
	echo " *** updating android sdk";
	$WORKSPACE/$ANDROID_SDK/tools/android update adb;
	$WORKSPACE/$ANDROID_SDK/tools/android update sdk;	
fi

if [ ! -e $WORKSPACE/$WEBDRIVER ];
then
	wget $WEBDRIVER_DOWNLOAD_URL/$WEBDRIVER --output-document $WORKSPACE/$WEBDRIVER;
fi
