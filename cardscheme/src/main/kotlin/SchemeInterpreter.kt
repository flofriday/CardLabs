class SchemeInterpreter {

    private var env = Environment()

    fun run(program: String): SchemeValue {
        val tokens = Tokenizer().tokenize(program)
        //println("Tokens:")
        //println(tokens)
        val ast = Parser().parse(tokens)
        //println("AST: ")
        //print(ast.dump())
        return Executor().execute(ast, env)
    }
}