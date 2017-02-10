1. RXTX is 32-bit library, so Java 32-bit must be used. I use Java 1.8 32-bit.
2. Copy rxtxSerial.dll into %JAVA_HOME%/bin. Not sure weather it needs to be under JDK or JRE folder though.
3. If Windows does not recognize Serial device, the drivers must be install. Once extracted CH341SER.ZIP, Run SETUP.EXE

4. Serial Port properties can be configured in serialport.properties.
5. Build package: mvn package -DskipTests
6. Startup Web Server: java -jar target/weight-reader-0.1.0.jar
7. Service URL: http://localhost:8080/readWeight