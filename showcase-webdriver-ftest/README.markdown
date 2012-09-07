Executing Tests
====================

Tests need the device with Selenium server running. Just type into to command line:

	$ mvn clean verify -P<mobile|desktop>,<android|firefox|htmlUnit|opera> -Dandroid.sdk.home=<directory>

Available Properties
--------------------

 * `android.sdk.home = <directory>`
    - sets a directory where your installation of Android SDK is located
 * `context.root = <path>`
    - context root where the tested application is running
 * `context.path = <path>`
    - context path to the tested application
 * `skin.name = <blueSky|classic|deepMarine|emeraldTown|japanCherry|ruby|wine>`
    - sets a skin which is used on the test pages
 * `webdriver.element.tries = <number>`
    - sets a number of tries which is used when an element is stale and tries to refresh its reference
 * `webdriver.host = <full path>`
    - sets a host which is used to control the active webdriver
 * `webdriver.timeout = <timout in seconds>`
    - sets an amount of time in seconds which is used for timeouts