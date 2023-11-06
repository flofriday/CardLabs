Compile `user.go` to `user.wasm`

```
GOOS=js GOARCH=wasm go build -o user.wasm user.go
```

and then run the main program

```
go run main.go
```


testing
```
./wasm2wat user.wasm -o user.wat
```