Executing Tests
====================

Tests needs the applications generated from archetypes. Just type into to command line:
	
	$ ./apps/richfaces-archetype-simpleapp.sh
	$ mvn clean verify -P<profile>
	
Available Profiles
--------------------
 
 * `jbossas-managed-7`
 * `tomcat-managed-6`
 * `tomcat-maneged-7`