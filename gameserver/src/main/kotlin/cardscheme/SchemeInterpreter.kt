package cardscheme

class SchemeInterpreter(
    private val outputBuffer: StringBuilder = StringBuilder(),
    /* private val timeLimitInSeconds: Long = 2,
    private val memoryLimit: Long = 1024 * 1024 * 1024,
    private val timeoutBetweenChecks: Long = 100,
     */
    private val instructionLimit: Long = 100000,
    private val memoryLimit: Long = 1024 * 1024,
) {
    var env = Environment(null, HashMap())
    val schemeSecurityMonitor = SchemeSecurityMonitor(instructionLimit, memoryLimit)

    init {
        injectBuiltin(env)
    }

    /**
     * Executes a given program with some time and memory constraints.
     */
    fun run(program: String): SchemeValue? {
        val tokens = Tokenizer().tokenize(program)
        return run(tokens)
    }

    /**
     * Executes a given program with some time and memory constraints.
     */
    fun run(tokens: List<Token>): SchemeValue? {
        val ast = Parser().parse(tokens)
        Resolver().resolve(ast)
        try {
            return Executor(env, outputBuffer, schemeSecurityMonitor).execute(ast)
        } catch (e: StackOverflowError) {
            throw SchemeError(
                "Stackoverflow",
                "You exceeded the stack limit, probably due to endless recursion. Look at the tip below for more.",
                null,
                "Look at the reason above for a tip."
            )
        }
    }

    /**
     * Executes a given function with some time and memory constraints.
     */
    fun run(
        func: CallableValue,
        args: List<SchemeValue>,
    ): SchemeValue? {
        try {
            return Executor(env, outputBuffer, schemeSecurityMonitor).execute(func, args)
        } catch (e: StackOverflowError) {
            throw SchemeError(
                "Stackoverflow",
                "You exceeded the stack limit, probably due to endless recursion. Look at the tip below for more.",
                null,
                "Look at the reason above for a tip."
            )
        }
    }

    /*
    private fun runWithTimeoutAndMemoryLimit(function: () -> SchemeValue?): SchemeValue? {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()
        val memoryMonitorThread = Thread(MemoryMonitor(Thread.currentThread(), memoryLimit, timeoutBetweenChecks))

        try {
            val future: Future<SchemeValue?> =
                executor.submit(
                    Callable {
                        function()
                    },
                )

            memoryMonitorThread.start()

            val result = future.get(timeLimitInSeconds, TimeUnit.SECONDS)
            memoryMonitorThread.interrupt()
            return result
        } catch (e: TimeoutException) {
            memoryMonitorThread.interrupt()
            throw SchemeError(
                "Function exceeded timeout",
                "The execution of the Interpreter was cancelled because it exceeded the timeout of $timeLimitInSeconds seconds",
                null,
                null,
            )
        } catch (e: InterruptedException) {
            throw SchemeError(
                "Function exceeded memory limit",
                "The execution of the Interpreter was cancelled because it exceeded the memory limit of $memoryLimit",
                null,
                null,
            )
        } catch (e: ExecutionException) {
            if (e.cause is SchemeError) {
                throw e.cause as SchemeError
            }
            throw e
        } finally {
            executor.shutdownNow()
        }
    }
     */
}
