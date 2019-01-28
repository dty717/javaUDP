cd ~
cd /usr/web/javaUDP/src/main/java/UDP/
javac -cp /usr/web/javaUDP/src/main/java/UDP/ -d out ServerListener.java 2> error_test.log
java -cp /usr/web/javaUDP/src/main/java/UDP/out  ServerListener > test.txt
