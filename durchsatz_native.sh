DEMO_COMMAND="build/native/nativeCompile/demo"


for i in {1..2}
do
perf stat --output "durchsatz_native_$i.txt" $DEMO_COMMAND
done