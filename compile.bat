javac -classpath "lib/jfreechart-1.0.14.jar;lib/jcommon-1.0.17.jar" *.java
jar cfm Driver.jar Manifest.MF *.class
java -jar Driver.jar