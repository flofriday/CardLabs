Compile the Rust library to wasm

```
wasm-pack build --target web
```

and then run the main program

```
go run main.go
```


To look at the WAT code
```
./wasm2wat hello_wasm_bg.wasm -o temp.wat 
```