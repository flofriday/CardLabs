package cardscheme

import MemoryMonitor
import java.util.concurrent.Callable
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class SchemeInterpreter(
    private val timeLimitInSeconds: Long = 2,
    private val memoryLimit: Long = 1024 * 1024 * 1024,
    private val timeoutBetweenChecks: Long = 100,
) {
    var env = Environment(null, HashMap())

    init {
        injectBuiltin(env)
    }

    /**
     * Executes a given program with some time and memory constraints.
     */
    fun run(program: String): SchemeValue? {
        val tokens = Tokenizer().tokenize(program)
        val ast = Parser().parse(tokens)
        val buffer = StringBuffer()
        return runWithTimeoutAndMemoryLimit({ Executor(env, buffer).execute(ast) })
    }

    /**
     * Executes a given program with some time and memory constraints.
     */
    fun run(tokens: List<Token>): SchemeValue? {
        val ast = Parser().parse(tokens)
        val buffer = StringBuffer()
        return runWithTimeoutAndMemoryLimit({ Executor(env, buffer).execute(ast) })
    }

    /**
     * Executes a given function with some time and memory constraints.
     */
    fun run(
        func: CallableValue,
        args: List<SchemeValue>,
    ): SchemeValue? {
        val buffer = StringBuffer()
        return runWithTimeoutAndMemoryLimit({ Executor(env, buffer).execute(func, args) })
    }

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
}
