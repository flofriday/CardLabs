fun main(args: Array<String>) {

    /*
    var interpreter = SchemeInterpreter()
    interpreter.run(program)
    val turnfunc = interpreter.getglobal("turn")
    val gamestate = magicfunction_tocreategamestate()
    val selected_card = interpreter.call(turnfunc, gamestate)
    */


    println("The CardLabs Scheme Interpreter")
    println("Made with ❤️ by CardLabs")
    var interpreter = SchemeInterpreter()
    while (true) {
        print(">>> ")
        var program = readln()

        var tokens = Tokenizer().tokenize(program)
        while (tokens.filter { t -> t is LParenToken }.size > tokens.filter { t -> t is RParenToken }.size) {
            print("... ")
            val newLine = readln()
            program += "\n" + newLine
            tokens = Tokenizer().tokenize(program)
        }

        val obj = interpreter.run(program)
        println(obj)
    }
}