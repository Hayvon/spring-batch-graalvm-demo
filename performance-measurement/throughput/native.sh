START_NATIVE="build/native/nativeCompile/demo"


for i in {1..10}
do
perf stat --output "throughput_native_$i.txt" $START_NATIVE
done