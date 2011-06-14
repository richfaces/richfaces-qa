Readme file for showcase-ftests:

The tests can be run from command line with this command, assuming that your current directory is the directory with showcase-ftests:
mvn clean verify -PnameOfTheProfile [-Dtest=nameOfTheTest] 

-nameOfTheProfile - for example jbossas-managed6, all profiles can be found in pom.xml
-nameOfTheTest - define the test which will be run, it is optional, when not assigned all tests are run

-note that you have to have assign correct path to the containers installations in src/test/resources/arquillian.xml
-I recommend you to use vncsession.sh script, to run tests in other desktop, since some of them are sensitive on mouse movements
-these tests require selenium-server running, which you can start manually with selenium-server.sh script, or you can let Arquillian framework
 do that for you, by enabling selenium-server extension in src/test/resources/arquillian.xml(default)

