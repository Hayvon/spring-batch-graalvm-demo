export PATH=$PATH:~/.local/bin

for i in {1..10}
do
psrecord "java -jar build/libs/demo-0.0.1-SNAPSHOT.jar" --include-children --interval 1 --duration 10 --plot "jvm_$i.png"
done