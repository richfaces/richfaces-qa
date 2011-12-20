#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

rm -rf $SCRIPT_DIR/richfaces-simpleapp;
rm -rf $SCRIPT_DIR/richfaces-gae;
rm -rf $SCRIPT_DIR/appengine-java-sdk;
rm -rf $SCRIPT_DIR/*.sh~;

