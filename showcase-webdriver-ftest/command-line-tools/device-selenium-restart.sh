 #!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
ANDROID_SDK=android-sdk-linux_x86;

WORKSPACE_DEFAULT=$SCRIPT_DIR/../android;

if [ -n "$1" ] && [ -d "$1" ]
then
        WORKSPACE=$1;
else
	WORKSPACE=$WORKSPACE_DEFAULT;
fi
echo " *** workspace set to [$WORKSPACE]";

echo " *** killing selenium server";
$WORKSPACE/$ANDROID_SDK/platform-tools/adb shell ps | grep org.openqa.selenium.android.app | awk '{print $2}' | xargs $WORKSPACE/$ANDROID_SDK/platform-tools/adb shell kill;
echo " *** starting selenium server";
$WORKSPACE/$ANDROID_SDK/platform-tools/adb shell am start -a android.intent.action.MAIN -n org.openqa.selenium.android.app/.MainActivity;
