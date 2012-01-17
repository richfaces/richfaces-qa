#!/bin/bash
mvn clean install -Pmyfaces,war-myfaces,war-jee6-bundled-mojarra,war-jee6-bundled-myfaces -Dselenium.server.skip=true -DskipTests=true $*
