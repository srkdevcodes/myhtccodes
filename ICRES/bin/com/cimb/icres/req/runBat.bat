@ECHO OFF
set CLASSPATH=.
set CLASSPATH=%CLASSPATH%;C:\Users\mycv2cv2\Downloads\log4j-1.2.17.jar;C:\Users\mycv2cv2\Downloads\commons-net-3.3.jar;C:\Users\mycv2cv2\Downloads\axis-1.4.jar;C:\Users\mycv2cv2\Downloads\ojdbc6-11.2.0.2.0.jar

"%JAVA_HOME%"\bin\java -cp -Xms128m -Xmx384m -Xnoclassgc D:\ICRES\ICRES\src\com\cimb\icres\req\GenReqXml.java

pause