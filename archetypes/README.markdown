Generting Tested Applications
====================

Firstly you need generate tested application. Go into `apps` folder:

 * `./clean.sh` removes all generated applications
 * `./richfaces-archetype-simpleapp.sh` generates simpleapp
 * `./richfaces-archetype-gae.sh` generates simpleapp and deploys it on Google App Engine and
 * `./version.sh` contains default version of RichFaces, version can be changed by setting the environment property `RICHFACES_VERSION`

Executing Tests
====================

Just type into to command line:
	
	$ mvn clean verify -P<profile>[,deploy-and-test]
	
Available Profiles
--------------------
 
 * `jbossas-managed-7`
 * `tomcat-managed-6`
 * `tomcat-maneged-7`
 * use profile `deploy-and-test` if you want to deploy the tested application via arquillian

Options
--------------------

 * `application.target.directory` - path to the WAR file which is deployed via arquillian
 * `context.root` - URL of tested application