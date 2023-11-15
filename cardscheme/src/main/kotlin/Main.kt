fun main(args: Array<String>) {

    /*
    var interpreter = SchemeInterpreter()
    interpreter.run(program)
    val turnfunc = interpreter.getglobal("turn")
    val gamestate = magicfunction_tocreategamestate()
    val selected_card = interpreter.call(turnfunc, gamestate)
    */
    val program = "(+ 1 2)"
    var interpreter = SchemeInterpreter()
    val obj = interpreter.run(program)
    println(obj)


    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}