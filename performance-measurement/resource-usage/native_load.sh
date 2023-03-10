export PATH=$PATH:~/.local/bin

for i in {1..1}
do
psrecord "./build/native/nativeCompile/demo" --include-children --interval 5 --duration 3600 --plot "native_1mio_$i.png" --log "native_1mio_$i.txt"
done