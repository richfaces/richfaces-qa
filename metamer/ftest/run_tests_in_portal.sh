#!/bin/bash
mvn clean verify -Pjbossportal-remote-71 -Dtemplates=plain -Dtest=richList.TestSimple -Darq.extension.graphene.seleniumSpeed=300
