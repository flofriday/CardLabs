package cardscheme

import MemoryMonitor
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
        return runWithTimeoutAndMemoryLimit({ Executor(env, buffer).execute(ast) })
    }

    /**
     * Executes a given function with some time and memory constraints.
     */
    fun run(func: CallableValue, args: List<SchemeValue>): SchemeValue? {
        val buffer = StringBuffer()
        return runWithTimeoutAndMemoryLimit({ Executor(env, buffer).execute(func, args) })
    }

    private fun runWithTimeoutAndMemoryLimit(function: () -> SchemeValue?, timeoutInSeconds: Long = 2, memoryLimitInMB : Long = 1024, timeoutBetweenChecks : Long=100): SchemeValue? {
        val executor: ExecutorService = Executors.newSingleThreadExecutor()

        try {
            val future: Future<SchemeValue?> = executor.submit(
                Callable {
                    function()
                },
            )
            val memoryMonitorThread = Thread(MemoryMonitor(Thread.currentThread(), memoryLimitInMB, timeoutBetweenChecks))
            memoryMonitorThread.start()

            val result = future.get(timeoutInSeconds, TimeUnit.SECONDS)
            memoryMonitorThread.interrupt()
            return result
        } catch (e: TimeoutException) {
            throw SchemeError("Function exceeded timeout", "The execution of the Interpreter was cancelled because it exceeded the timeout of $timeoutInSeconds seconds", null, null)
        } catch (e: InterruptedException) {
            throw SchemeError("Function exceeded memory limit", "The execution of the Interpreter was cancelled because it exceeded the memory limit of $memoryLimitInMB MB", null, null)
        } catch (e: ExecutionException) {
            if (e.cause is SchemeError) {
                throw e.cause as SchemeError
            }
            throw e
        } finally {
            executor.shutdownNow()
        }
    }
}
