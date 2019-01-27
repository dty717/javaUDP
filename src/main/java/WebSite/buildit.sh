cd ~
cd /usr/web//javaUDP/src/main/java/WebSite/src/test/ 
javac -cp "/usr/web//javaUDP/src/main/java/WebSite/out/lib/jsoup-1.8.1.jar":/usr/web/javaUDP/src/main/java/WebSite/src/test/ -d /usr/web/javaUDP/src/main/java/WebSite/out Testjsoup.java
cd ~
cd /usr/web/javaUDP/src/main/java/WebSite/src/
java -cp /usr/web/javaUDP/src/main/java/WebSite/out:"/usr/web/javaUDP/src/main/java/WebSite/out/lib/jsoup-1.8.1.jar" test.Testjsoup