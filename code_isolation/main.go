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

	wasm, err := os.ReadFile("user.wasm")
	check(err)

	//err = wasmtime.ModuleValidate(store.Engine, wasm)
	//check(err)

	module, err := wasmtime.NewModule(store.Engine, wasm)
	check(err)

	/*item := wasmtime.WrapFunc(store, func() {
        fmt.Println("Hello from Go!")
    })*/

	item1 := wasmtime.WrapFunc(store, func(i int32) {
    })


	instance, err := wasmtime.NewInstance(store, module, []wasmtime.AsExtern{item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1,item1})
	check(err)

	run := instance.GetFunc(store, "sum")
	result, err := run.Call(store, 6, 18)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("sum(6, 18) = %d\n", result.(int32))
}
