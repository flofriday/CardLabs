fun main(args: Array<String>) {
    val interpreter = SchemeInterpreter()
    println(interpreter.run("(+ 5 3)"))
}
