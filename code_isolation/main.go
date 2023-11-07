package main

import (
	"fmt"
	"log"
	"os"
	"github.com/bytecodealliance/wasmtime-go"
)

func check(e error) {
    if e != nil {
        log.Fatal(e)
    }
}

func main() {
	store := wasmtime.NewStore(wasmtime.NewEngine())

	wasm, err := os.ReadFile("./pkg/code_isolation_bg.wasm")
	check(err)

	//err = wasmtime.ModuleValidate(store.Engine, wasm)
	//check(err)

	module, err := wasmtime.NewModule(store.Engine, wasm)
	check(err)

	instance, err := wasmtime.NewInstance(store, module, []wasmtime.AsExtern{})
	check(err)

	run := instance.GetFunc(store, "sum")
	result, err := run.Call(store, 6, 18)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("sum(6, 18) = %d\n", result.(int32))
}
