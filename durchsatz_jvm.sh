DEMO_COMMAND="java -jar build/libs/demo-0.0.1-SNAPSHOT.jar"


for i in {1..2}
do
perf stat --output "durchsatz_jvm_$i.txt" $DEMO_COMMAND
done