Executing tests
====================

Just type into to command line: 

	$ mvn clean verify -P <profile> -D arquillian.launch <profile> [-D webdriver.android]

Available profiles:

 * jbossas-managed-7
 
Description of available properties

 * webdriver.android - enables testing through the android emulator
 
Command Line Tools
====================

 * device.sh - launches the android emulator 
 * fedora-64-packages.sh - installs needed libraries on Fedora 64bit
 * android-sdk.sh - downloads and updates needed files such as android-sdk and android web driver