Generating Tested Applications
====================

Firstly you need generate tested application. Go into `apps` folder:

 * `./clean.sh` removes all generated applications
 * `./richfaces-archetype-simpleapp.sh` generates simpleapp
 * `./richfaces-archetype-gae.sh` generates simpleapp and deploys it on Google App Engine and
 * `./version.sh` contains default version of RichFaces, version can be changed by setting the environment property `RICHFACES_VERSION`

Executing Tests
====================

The tests are run the same way as tests in Metamer (see its README for more details):

	$ mvn clean verify -P<profile> -Dbrowser=<firefox|chrome|ie>

E.g. on WildFly 10 with Firefox browser:

	$ mvn clean verify -Pwildfly-managed-10-0 -Dbrowser=firefox
	
Available Profiles
--------------------

See profiles section in parent pom (richfaces-qa/pom.xml). All managed profiles are available with exception for the kitchensink application, where it cannot run on Tomcat (no internal DB).
