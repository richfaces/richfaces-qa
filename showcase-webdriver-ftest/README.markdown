Executing Tests
====================

Tests need the device with Selenium server running. Just type into to command line: 

	$ mvn clean verify -D webdriver.android -D android.skd.dir=<directory>
 
Available Properties
--------------------

 * `android.sdk.dir = <directory>`
    - sets a directory where your installation of Android SDK is located
 * `android.sdk.sendkey.delay = <amount of time>`
    - sets an amount of time in miliseconds which is used for delay after sending keys
      to Android SDK
 * `context.root = <path>`
    - context root where the tested application is running
 * `context.path = <path>`
    - context path to the tested application
 * `skin.name = <blueSky|classic|deepMarine|emeraldTown|japanCherry|ruby|wine>
    - sets a skin which is used on the test pages
 * `webdriver.android`
    - enables testing through the android emulator
 * `webdriver.host = <full path>`
    - sets a host which is used to control the active webdriver
 * `webdriver.capabilities = <android|chrome|firefox|internetExplorer|iphone|opera>`
    - sets browser capabilities, it's used only when the default webdriver
      (HtmlUnitDriver) is active

 
Command Line Tools
====================

 * `device.sh <directory>`
    - launches a new device on android emulator with Selenium server running
    - only one device can be running at one moment
    - <directory> has to contain a directory with Android SDK
 * `fedora-64-packages.sh`
    - installs needed libraries on Fedora 64bit
 * `android-sdk.sh [<directory>]`
    - downloads and updates needed files such as android-sdk and android web driver
