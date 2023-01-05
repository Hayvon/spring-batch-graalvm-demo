export PATH=$PATH:~/.local/bin
psrecord "java -jar build/libs/demo-0.0.1-SNAPSHOT.jar" --include-children --interval 1 --duration 900 --plot jvm_1mio.png