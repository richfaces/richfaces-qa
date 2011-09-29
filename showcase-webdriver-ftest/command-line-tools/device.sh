 #!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
DEVICE_SERIAL=emulator-5554;
DEVICE_VERSION_DEFAULT=13;
DEVICE_NAME=metamer;
DEVICE_MEMORY=500M;
WEBDRIVER=android-server-2.6.0.apk;
WEBDRIVER_DEVICE_PORT=8080;
WEBDRIVER_HOST_PORT=4444;
ANDROID_SDK=android-sdk-linux_x86;

WORKSPACE_DEFAULT=$SCRIPT_DIR/../android;

echo " *** starting device using android sdk";

if [ -n "$1" ] && [ -d "$1" ]
then
        WORKSPACE=$1;
else
	WORKSPACE=$WORKSPACE_DEFAULT;
fi
echo " *** workspace set to [$WORKSPACE]";

if [ -n "$2" ]
then
        DEVICE_VERSION=$2;
else
	DEVICE_VERSION=$DEVICE_VERSION_DEFAULT;
fi
echo " *** device version set to [$DEVICE_VERSION]";


echo " *** deleting old devices";
$WORKSPACE/$ANDROID_SDK/tools/android delete avd -n $DEVICE_NAME;
echo " *** creating new device";
$WORKSPACE/$ANDROID_SDK/tools/android create avd -n $DEVICE_NAME -t $DEVICE_VERSION -c $DEVICE_MEMORY;
echo " *** starting device";
($WORKSPACE/$ANDROID_SDK/tools/emulator -avd my_android -no-audio -no-boot-anim &);
while [[ "$GREPPED" != $DEVICE_SERIAL*[d]* ]];
do
	GREPPED=`$WORKSPACE/$ANDROID_SDK/platform-tools/adb devices | grep $DEVICE_SERIAL`;
done
echo " *** installing web driver [$WEBDRIVER]"
$WORKSPACE/$ANDROID_SDK/platform-tools/adb -s $DEVICE_SERIAL -e install -r $WORKSPACE/$WEBDRIVER;
echo " *** forwarding tcp: host port $WEBDRIVER_HOST_PORT -> device port $WEBDRIVER_DEVICE_PORT";
$WORKSPACE/$ANDROID_SDK/platform-tools/adb -s $DEVICE_SERIAL forward tcp:$WEBDRIVER_HOST_PORT tcp:$WEBDRIVER_DEVICE_PORT;
echo " *** unlocking screen"
$WORKSPACE/$ANDROID_SDK/platform-tools/adb shell input keyevent 82;
echo " *** starting web driver";
$WORKSPACE/$ANDROID_SDK/platform-tools/adb shell am start -a android.intent.action.MAIN -n org.openqa.selenium.android.app/.MainActivity;

