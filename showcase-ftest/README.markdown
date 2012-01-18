Readme file for showcase-ftests

How to run tests:
=================

The tests can be run from command line with this command, assuming that your current directory is the directory with showcase-ftests:

**mvn clean verify -PnameOfTheProfile [-Dtest=nameOfTheTest] [-Dshowcase.classifier=classifier] [-Darquillian.launch=whichSettingsForContainer]**

- `nameOfTheProfile` - for example jbossas-managed-6, all profiles can be found in pom.xml

- `nameOfTheTest` - define the test which will be run, it is optional, when not assigned all tests are run

- `showcase.classifier` - for various containers there is need to deploy various showcase wars, jee6(default)

- `whichSettingsForContainer` - when this is set, the configuration for container with qualifier whichSettingsForContainer will be launched, different qualifiers 
	can be set in arquillian.xml

Notes:
======
-note that binary distributions for all containers are downloaded automatically, either as maven dependency or as ant download. 
 You can also use your binaries, but you have to assign correct path to the containers installations in src/test/resources/arquillian.xml

-It is recommended to use vncsession.sh script, to run tests in other desktop, since some of them are sensitive on mouse movements, clicks etc.

-these tests require selenium-server running, which you can start manually with selenium-server.sh script, or you can let Arquillian framework
 do that for you, by setting the skip property to false(default) for selenium-server qualifier in src/test/resources/arquillian.xml

