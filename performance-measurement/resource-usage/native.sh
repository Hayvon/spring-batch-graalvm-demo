export PATH=$PATH:~/.local/bin

for i in {1..10}
do
psrecord "build/native/nativeCompile/demo" --include-children --interval 0.001 --duration 3 --plot "native_$i.png" --log "native_$i.txt"
done