package main

import "syscall/js"

func sum(this js.Value, args []js.Value) any {
	//return a + b

    return 7
}


func main() {
    js.Global().Set("sum", js.FuncOf(sum))
    <-make(chan bool)

}
