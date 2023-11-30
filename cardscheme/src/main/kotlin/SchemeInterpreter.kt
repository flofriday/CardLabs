class SchemeInterpreter() {
    var env = Environment(null, HashMap())

    fun getStdOut(): String {
        TODO()
    }

    init {
        injectBuiltin(env)
    }

    /**
     * Executes a given program with some time and memory constraints.
     */
    fun run(program: String): SchemeValue? {
        val tokens = Tokenizer().tokenize(program)
        // println("Tokens:")
        // println(tokens)
        val ast = Parser().parse(tokens)
        // println("AST: ")
        // print(ast.dump())
        val buffer = StringBuffer()
        return Executor(env, buffer).execute(ast)
    }

    /**
     * Executes a given function with some time and memory constraints.
     */
    fun run(func: CallableValue, args: List<SchemeValue>): SchemeValue? {
        val buffer = StringBuffer()
        return Executor(env, buffer).execute(func, args)
    }
}
