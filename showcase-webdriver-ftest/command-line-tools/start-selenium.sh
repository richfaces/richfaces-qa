#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;

source "$SCRIPT_DIR/config.sh";

ARG_ADB=$1;

WORKSPACE=/tmp

if [ ! ${ARG_ADB} ]; then
	exit 1;
fi

msg "starting web driver";
$ARG_ADB shell am start -a android.intent.action.MAIN -n org.openqa.selenium.android.app/.MainActivity;
