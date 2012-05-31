Executing Tests
====================

Tests need the device with Selenium server running. Just type into to command line:

	$ mvn clean verify -P<container> -Dandroid.sdk.home=<directory>

Available Properties
--------------------

 * `android.sdk.home = <directory>`
    - sets a directory where your installation of Android SDK is located
 * `android.sdk.inpath`
    - means the Android SDK binaries are in the PATH
 * `android.sdk.sendkey.delay = <amount of time>`
    - sets an amount of time in miliseconds which is used for delay after sending keys
      to Android SDK
 * `context.root = <path>`
    - context root where the tested application is running
 * `context.path = <path>`
    - context path to the tested application
 * `showcase.layout = <common|mobile>`
    - sets a showcase version
    - if the property is not set, the web driver type determines showcase version
 * `skin.name = <blueSky|classic|deepMarine|emeraldTown|japanCherry|ruby|wine>`
    - sets a skin which is used on the test pages
 * `webdriver.element.tries = <number>`
    - sets a number of tries which is used when an element is stale and tries to refresh its reference
 * `webdriver.host = <full path>`
    - sets a host which is used to control the active webdriver
 * `webdriver.timeout = <timout in seconds>`
    - sets an amount of time in seconds which is used for timeouts