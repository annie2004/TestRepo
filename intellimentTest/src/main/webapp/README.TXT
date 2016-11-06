DEMO: INTELLIMENTTEST / 06112016 / TIME SPENT: 18 HOURS BY @NA

Before run intellimentTest check:
--------------------------------------------------------
MAVEN INSTALLED:

   	apache-maven-3.X.X
---------------------------------------------------------   	
JDK INSTALLED:

	jdk1.8.X.X
---------------------------------------------------------

FILE --> configIntelli.properties                       (Configure application intellimentTest)
     PROPERTY --> intelliment.rutefile.rules           (The application "intellimentTest" will work whith this file 
     													you can put your own file. This property is a route. Default 
     													intellimentTest\src\main\resources is host. Check your file is hosted 
     													there. INTELLIMENTTEST 0.0.1 DOESNT WORK IF FIND BLANK LINES!)
FILE --> TestConfigIntelli.properties                   (Configure properties for unit test)
     PROPERTY --> intelliment.test.endpoint.address    (Endpoint for testing)
	 PROPERTY --> intelliment.test.wadl.address        (wadl address for testing)
	 PROPERTY --> intelliment.test.jsonRequest         (stub json for unit test)
	 PROPERTY --> intelliment.test.knownIdjsonResponse (id for request rule and assert response in unit test)
     PROPERTY --> intelliment.test.numLines.txt        (number of lines of txt working on for assert in unit test)
     
FILE --> log4j.properties
	 PROPERTY --> log4j.appender.stdout.File           (Absolute path of intellimentTest log file)
	 PROPERTY --> log4j.rootLogger                     (Level of log INFO, ERROR, DEBUG, TRACE)
---------------------------------------------------------

RUNNING:
     --> Open console command and go to directory where the pom.xml "intellimentTest" is.
     --> execute mvn jetty:run
     --> you have get Started Jetty Server
     
EXERCISE 1:

   http://localhost:8080/intelliment/acl
   
EXERCISE 2:

   http://localhost:8080/intelliment/acl/{idOfRule}

EXERCISE 3:

   You can use postman (free google app)
   Data in postman:
   http://localhost:8080/intelliment/acl 
   POST -request raw - app/json -     {
										"source": "52.71.25.125",
										"destination": "75.129.160.175",
										"protocol": "any"
										}

   This request body json, accept "any" in every data of json, but isnt useful, because "intellimentTest"
   doesnt search the string "any". "any" find first valid ip.