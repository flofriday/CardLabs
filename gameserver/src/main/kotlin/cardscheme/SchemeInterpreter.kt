package cardscheme

import java.util.concurrent.*

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
        return runWithTimeout({ Executor(env, buffer).execute(ast) })
    }

    /**
     * Executes a given function with some time and memory constraints.
     */
    fun run(func: CallableValue, args: List<SchemeValue>): SchemeValue? {
        val buffer = StringBuffer()
        return runWithTimeout({ Executor(env, buffer).execute(func, args) })
    }

    private fun runWithTimeout(function: () -> SchemeValue?, timeoutInSeconds: Long = 2): SchemeValue? {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        try {
            val future: Future<SchemeValue?> = executor.submit(
                Callable {
                    function()
                },
            )

            return future.get(timeoutInSeconds, TimeUnit.SECONDS) // Timeout set to 2 seconds
        } catch (e: TimeoutException) {
            throw SchemeError("Function exceeded timeout", "The execution of the Interpreter was cancelled because it exceeded the timeout of $timeoutInSeconds seconds", null, null)
        } catch (e: ExecutionException) {
            if (e.cause is SchemeError) {
                throw e.cause as SchemeError
            }
            throw e
        }
    }
}
