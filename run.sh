mvn package
java -Xmx9G \
-cp lib/marytts-client-5.2-SNAPSHOT-jar-with-dependencies.jar:lib/marytts-runtime-5.2-SNAPSHOT-jar-with-dependencies.jar:lib/marytts-lang-de-5.2-SNAPSHOT.jar:lib/marytts-lang-en-5.2-SNAPSHOT.jar:lib/voice-bits3-5.1.jar:lib/voice-cmu-slt-hsmm-5.2-SNAPSHOT.jar:target/wikipediabrowser-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
main.StartUp
