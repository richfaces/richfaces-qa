#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;

source "$SCRIPT_DIR/config.sh";

msg "deleting old devices";
$ARG_ANDROID_SDK/tools/android delete avd -n $ARG_DEVICE_NAME;

msg "creating new device";
$ARG_ANDROID_SDK/tools/android create avd -n $ARG_DEVICE_NAME -t $ARG_DEVICE_VERSION -c $DEVICE_MEMORY;

msg "starting device";
($ARG_ANDROID_SDK/tools/emulator -avd my_android -no-audio -no-boot-anim &);
while [[ "$GREPPED" != $DEVICE_SERIAL*[d]* ]];
do
	GREPPED=`$ARG_ANDROID_SDK/platform-tools/adb devices | grep $DEVICE_SERIAL`;
done

if [ $ARG_INSTALL_WEBDRIVER == 1 ]; then
	WEBDRIVER=${WEBDRIVER_FILE_PREFIX}${ARG_WEBDRIVER_FILE_VERSION}${WEBDRIVER_FILE_SUFFIX};
	msg "installing web driver [$WEBDRIVER]";
	wget $WEBDRIVER_DOWNLOAD_URL/$WEBDRIVER --output-document $ARG_WORKSPACE/$WEBDRIVER;
	$ARG_ANDROID_SDK/platform-tools/adb -s $DEVICE_SERIAL -e install -r $WORKSPACE/$WEBDRIVER;
	msg "forwarding tcp: host port $ARG_HOST_PORT -> device port $ARG_DEVICE_PORT";
	$ARG_ANDROID_SDK/platform-tools/adb -s $DEVICE_SERIAL forward tcp:$ARG_HOST_PORT tcp:$ARG_DEVICE_PORT;
fi

msg "unlocking screen";
$ARG_ANDROID_SDK/platform-tools/adb shell input keyevent 82;

if [ $ARG_INSTALL_WEBDRIVER == 1 ]; then
	msg "starting web driver";
	$ARG_ANDROID_SDK/platform-tools/adb shell am start -a android.intent.action.MAIN -n org.openqa.selenium.android.app/.MainActivity;
fi
