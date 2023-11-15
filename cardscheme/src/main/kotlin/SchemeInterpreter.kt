class SchemeInterpreter {

    private var env = Environment()

    fun run(program: String): SchemeValue {
        val tokens = Tokenizer().tokenize(program)
        print(tokens)
        //val ast = parse(tokens)
        //return execute(ast, env)
        return IntValue(42)
    }
}