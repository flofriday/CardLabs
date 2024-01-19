package cardscheme

class MemoryMonitor(
    private val threadToInterrupt: Thread,
    private val memoryLimit: Long,
    private val timeoutBetweenChecks: Long,
) : Runnable {
    override fun run() {
        try {
            while (!Thread.currentThread().isInterrupted) {
                val memoryUsage =
                    (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())
                if (memoryUsage > memoryLimit) {
                    threadToInterrupt.interrupt()
                    break
                }

                Thread.sleep(timeoutBetweenChecks)
            }
        } catch (e: InterruptedException) {
            // Thread interrupted, stopping monitoring
        }
    }
}
