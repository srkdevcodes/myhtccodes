@echo OFF
set CLASSPATH=%CLASSPATH%;%~dp0\C:\Users\mycv2cv2\Downloads\ojdbc6-11.2.0.2.0.jar
set CLASSPATH=%CLASSPATH%;%~dp0\C:\Users\mycv2cv2\Downloads\log4j-1.2.17.jar
set CLASSPATH=%CLASSPATH%;%~dp0\C:\Users\mycv2cv2\Downloads\commons-net-3.3.jar
set CLASSPATH=%CLASSPATH%;%~dp0\C:\Users\mycv2cv2\Downloads\axis-1.4.jar
set CLASSPATH=%CLASSPATH%;%~dp0\C:\Users\mycv2cv2\Downloads\apache-logging-log4j.jar


"%JAVA_HOME%"\bin\javac *.java

"%JAVA_HOME%"\bin\java GenReqXml
pause
