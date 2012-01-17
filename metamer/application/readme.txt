Testapp is an application intended to test all RichFaces components on their own, inside other JSF components and in various environments. It contains set of small use-cases implemented using RichFaces components.


1 Building and Running the application


1.1. Requirements

- Maven 2.1.0 or later
- Tomcat 6, JBoss 6
- JDK 1.6

1.2 Optional Additional Software
- Eclipse IDE + JBoss Tools (to explore and run the application in IDE).
- You're free to use any other IDE also but we haven't tested an application in other environments. 


1.3 Building the application for Tomcat

To build a binary for Tomcat, you need to run

   mvn clean package

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. You can deploy it on the server by copying .war file from 'target' folder to 
the TOMCAT_HOME/webapps folder. Then, launch the startup.sh or startup.bat script from TOMCAT_HOME/bin/ directory to start the server.


1.4 Building the application for JEE6 containers

To build a binary for JEE6 container, run

   mvn clean package -P jee6

When you see the BUILD SUCCESSFUL message you can deploy the application on the server. You can deploy it on the server by copying .war file from 'target' folder to
the JBOSS_HOME/server/default/deploy folder. Then, launch the run.sh or run.bat script from JBOSS_HOME/bin/ directory to start the server.


1.5 Building and running from Eclipse IDE

In order to explore, run and deploy the application in Eclipse IDE you can use one of the following options:

  * Just import as maven project if using m2eclipse plugin.

  * without m2eclipse - build it with the following command and import as an existing project.
  
      mvn clean install eclipse:clean eclipse:eclipse
  

