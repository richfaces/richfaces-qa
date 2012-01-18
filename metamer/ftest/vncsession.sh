#!/bin/bash
vncserver :1
nohup vncviewer :1 &>/dev/null &
export DISPLAY=:1
