How to build metamer application:
=================================

* just run the build.sh script and see the generated metamer wars in application/target, for more information please see the: https://community.jboss.org/wiki/Metamer

How to run functional tests:
============================

* your current directory should be ftest-source and you need to run command:

**mvn clean verify -PnameOfTheProfile [-Dtest=nameOfTheTest] [-Dmetamer.classifier=classifier] [-Darquillian.launch=whichSettingsForContainer] [-Dtomcat6Zip=path/to/your/tomcat6/zip] [-Dtomcat7Zip=path/to/your/tomcat7/zip]**

- `nameOfTheProfile` - for example jbossas-managed-7, all profiles can be found in ftest-source/pom.xml

- `nameOfTheTest` - define the test which will be run, it is optional, when not assigned all tests are run

- `metamer.classifier` - for various containers there is need to deploy various metamer wars, jee6(default)

- `whichSettingsForContainer` - when this is set, the configuration for container with qualifier whichSettingsForContainer will be launched, different qualifiers 
	can be set in ftest-source/src/test/resources/arquillian.xml

- `tomcat6Zip` and `tomcat7Zip` flags are mandatory in case you want to test on Tomcat 6 or 7 respectively, the path should point to the Tomcat distribution zip file, versions of Tomcat distributions are defined in the parent pom.xml

Notes:
======
- note that binary distributions for JBoss containers are downloaded automatically as maven dependencies. You can also use your binaries, but you have to assign correct path to the containers installations in src/test/resources/arquillian.xml

- when testing on Tomcats 6 or 7, one has to set `-Dtomcat6Zip` or `-Dtomcat7Zip` respectively, to point to zipped Tomcat distributions

- It is recommended to use vncsession.sh script, to run tests in other desktop, since some of them are sensitive on mouse movements, clicks etc.
