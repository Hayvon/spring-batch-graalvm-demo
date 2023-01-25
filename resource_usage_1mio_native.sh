export PATH=$PATH:~/.local/bin

for i in {1..10}
do
psrecord "build/native/nativeCompile/demo" --include-children --interval 1 --duration 7200 --plot "native_1mio_$i.png" --log "native_1mio_$i.txt"
done