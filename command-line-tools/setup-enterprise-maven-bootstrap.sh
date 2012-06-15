#!/bin/bash
SCRIPT_DIR=`dirname $BASH_SOURCE`;
SCRIPT_DIR=`readlink -f $SCRIPT_DIR`;

usage()
{
cat << EOF

---------------------------------------------------------------------------------
  usage: $0
      [url of EAP maven repository ZIP archive]
      [url of WFK maven repository ZIP archive]

  This script setups the enterprise maven bootstrap submodule
---------------------------------------------------------------------------------

EOF
}

# Check params
if [ "$1" == "" ]; then
    usage;
    exit 1;
fi
if [ "$2" == "" ]; then
    usage;
    exit 1;
fi

# Init maven bootstrap
cd $SCRIPT_DIR/..;
rm -rf enterprise-maven-bootstrap;
git clone git://github.com/jboss-eap/maven-bootstrap.git enterprise-maven-bootstrap;
cd enterprise-maven-bootstrap;
git checkout fdec9804a92f7b81ea7784ed836ff23dffe087aa;
cd $SCRIPT_DIR/..;

# Remove old files
if [ -f "$SCRIPT_DIR/settings.xml" ]; then
    rm -rf "$SCRIPT_DIR/settings.xml";
fi
rm -rf $SCRIPT_DIR/../enterprise-maven-bootstrap/jboss*repository


# Download and unpack repositories
cd $SCRIPT_DIR/../enterprise-maven-bootstrap;
EAP_ARCHIVE=`basename $1`;
WFK_ARCHIVE=`basename $2`;
EAP_DIRECTORY=`basename $EAP_ARCHIVE .zip`;
WFK_DIRECTORY=`basename $WFK_ARCHIVE .zip`;
if [ ! -f $EAP_ARCHIVE ]; then
    wget $1 --no-check-certificate;
fi
if [ ! -f $WFK_ARCHIVE ]; then
    wget $2 --no-check-certificate;
fi

unzip $EAP_ARCHIVE;
unzip $WFK_ARCHIVE;

#rm -rf *.zip;

# Package the maven bootstrap
mvn clean package -Deap6.enterprise.repository.zip=$SCRIPT_DIR/../enterprise-maven-bootstrap/$EAP_ARCHIVE -Dwfk2.enterprise.repository.zip=$SCRIPT_DIR/../enterprise-maven-bootstrap/$WFK_ARCHIVE -Deap6.enterprise.repository.dir=$SCRIPT_DIR/../enterprise-maven-bootstrap/$EAP_DIRECTORY -Dwfk2.enterprise.repository.dir=$SCRIPT_DIR/../enterprise-maven-bootstrap/$WFK_DIRECTORY
cd $SCRIPT_DIR/..;
