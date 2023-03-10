START_JAR="java -jar build/libs/demo-0.0.1-SNAPSHOT.jar"

for i in {1..10}
do
perf stat --output "throughput_jvm_$i.txt" $START_JAR
done