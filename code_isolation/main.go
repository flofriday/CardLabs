package main

import (
	"fmt"
	"log"
	"os"
	"github.com/bytecodealliance/wasmtime-go"
)

func main() {
	store := wasmtime.NewStore(wasmtime.NewEngine())
	/*wasm, err := wasmtime.Wat2Wasm(`
	(module
	  (func $gcd (param i32 i32) (result i32)
	    (local i32)
	    block  ;; label = @1
	      block  ;; label = @2
	        local.get 0
	        br_if 0 (;@2;)
	        local.get 1
	        local.set 2
	        br 1 (;@1;)
	      end
	      loop  ;; label = @2
	        local.get 1
	        local.get 0
	        local.tee 2
	        i32.rem_u
	        local.set 0
	        local.get 2
	        local.set 1
	        local.get 0
	        br_if 0 (;@2;)
	      end
	    end
	    local.get 2
	  )
	  (export "gcd" (func $gcd))
	)
	`)
	*/

	wasm, err := os.ReadFile("user.wasm")
	if err != nil {
		log.Fatal(err)
	}
	log.Println("Loaded")
	log.Printf("%v", len(wasm))

	err = wasmtime.ModuleValidate(store.Engine, wasm)
	if err != nil {
		log.Println("INVALID:")
		log.Fatal(err)
	}
	module, err := wasmtime.NewModule(store.Engine, wasm)
	if err != nil {
		log.Fatal(err)
	}
	log.Println("Moduled")
	log.Println("%v", len(module.Exports()))
	instance, err := wasmtime.NewInstance(store, module, []wasmtime.AsExtern{})
	if err != nil {
		log.Fatal(err)
	}
	log.Println("Instanced")
	run := instance.GetFunc(store, "sum")
	result, err := run.Call(store, 6, 18)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Printf("sum(6, 18) = %d\n", result.(int32))
}
