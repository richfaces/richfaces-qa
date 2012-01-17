How to build metamer application:
=================================

- just run the build.sh script and see the generated metamer wars in application/target, for more information please see the: https://community.jboss.org/wiki/Metamer

How to run functional tests:
============================

- your current directory should be ftest-source and you need to run command:

**mvn clean verify -PnameOfTheProfile [-Dtest=nameOfTheTest] [-Dmetamer.classifier=classifier] [-Darquillian.launch=whichSettingsForContainer]**

-nameOfTheProfile - for example jbossas-managed-7, all profiles can be found in ftest-source/pom.xml

-nameOfTheTest - define the test which will be run, it is optional, when not assigned all tests are run

-metamer.classifier - for various containers there is need to deploy various metamer wars, jee6(default)

-whichSettingsForContainer - when this is set, the configuration for container with qualifier whichSettingsForContainer will be launched, different qualifiers 
	can be set in ftest-source/src/test/resources/arquillian.xml

Notes:
======
-note that binary distributions for all containers are downloaded automatically, either as maven dependency or as ant download. 
 You can also use your binaries, but you have to assign correct path to the containers installations in src/test/resources/arquillian.xml

-It is recommended to use vncsession.sh script, to run tests in other desktop, since some of them are sensitive on mouse movements, clicks etc.
