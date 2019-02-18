
cd ~
cd /usr/web/javaUDP/src/main/java/UDPConnection/
javac -cp /usr/web/javaUDP/src/main/java/UDPConnection/ -d out GoogleApi.java 2> error_test.log
java -cp /usr/web/javaUDP/src/main/java/UDPConnection/out  UDPConnection.GoogleApi > test.txt
