class MemoryMonitor(private val threadToInterrupt: Thread, private val memoryLimitInMB: Long, private val timeoutBetweenChecks:Long) : Runnable {
    override fun run() {
        try {
            while (!Thread.currentThread().isInterrupted) {
                val memoryUsageInMB = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)
                if (memoryUsageInMB > memoryLimitInMB) {
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
