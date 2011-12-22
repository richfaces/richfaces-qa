#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

source "$SCRIPT_DIR/config.sh";

ARG_ADB=$1;

WORKSPACE=/tmp

if [ ! ${ARG_ADB} ]; then
	exit 1;
fi

msg "starting web driver";
for ((I=0; I<$TIMOUT_TRIES; I++)); do
    msg "... try $I";
    GREPPED=`$ARG_ADB shell am start -a android.intent.action.MAIN -n org.openqa.selenium.android.app/.MainActivity | grep Error`;
    if [ "$GREPPED" == "" ]; then
        INSTALLED=1;
        msg "... success";
        break;
    else
        msg "... failed";
    fi
    sleep $SLEEP_AMOUNT;
done

if [ $INSTALLED == "" ]; then
    msg "starting of web driver failed";
    exit 1;
fi
