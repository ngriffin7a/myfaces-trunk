Building and deploying the example:
1) download and install tomcat 4.1.x or greater
  1.a) If you have not already you need to add a user that has the manager and admin roles
2) add the catalina-ant.jar file to your ant classpath
  2.a) The catalina-ant.jar file is $TOMCAT/server/lib directory
  2.b) You can copy catalina-ant.jar to your ant/lib directory or you can modify the ant script to include the jar.
  2.c) IntelliJ-users: add it as "Additional Classpath" in the "Build File Properties".
3) Copy build.properties.sample file to build.properties and set the values
4) Execute the install target to have the examples installed

Bill Dudney [bdudney@mac.com]