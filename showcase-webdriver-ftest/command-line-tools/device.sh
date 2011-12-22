#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

source "$SCRIPT_DIR/device-arguments.sh";
if [ $ARG_START_DEVICE == 1 ]; then

	msg "creating new device";
	$ARG_ANDROID_SDK/tools/android create avd -n $ARG_DEVICE_NAME -t $ARG_DEVICE_VERSION -c $DEVICE_MEMORY  --path "$ARG_WORKSPACE" --force;

	msg "starting device";
	($ARG_ANDROID_SDK/tools/emulator -avd $ARG_DEVICE_NAME -no-audio -no-boot-anim&);
	while [[ "$GREPPED" != $DEVICE_SERIAL*[d]* ]];
	do
		GREPPED=`$ARG_ANDROID_SDK/platform-tools/adb devices | grep $DEVICE_SERIAL`;
        sleep $SLEEP_AMOUNT;
	done

fi

if [ $ARG_INSTALL_WEBDRIVER == 1 ]; then
    ${SCRIPT_DIR}/install-selenium.sh "$ARG_ANDROID_SDK/platform-tools/adb" $DEVICE_SERIAL $ARG_DEVICE_PORT $ARG_HOST_PORT $ARG_WEBDRIVER_FILE_VERSION;
fi

if [ $ARG_INSTALL_WEBDRIVER == 1 ]; then
	${SCRIPT_DIR}/start-selenium.sh "$ARG_ANDROID_SDK/platform-tools/adb";
fi

msg "unlocking screen";
$ARG_ANDROID_SDK/platform-tools/adb shell input keyevent 82;
