#!/bin/bash
SCRIPT_DIR=$( cd "$( dirname "$0" )" && pwd );

rm -rf $SCRIPT_DIR/richfaces-simpleapp;
rm -rf $SCRIPT_DIR/richfaces-gae;
rm -rf $SCRIPT_DIR/appengine-java-sdk;
rm -rf $SCRIPT_DIR/*.sh~;

