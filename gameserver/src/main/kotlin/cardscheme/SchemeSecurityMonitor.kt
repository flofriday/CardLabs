package cardscheme

class SchemeSecurityMonitor(val instructionLimit: Long, val memoryLimit: Long) {
    private var instructionCount = 0L
    private var memoryCount = 0L

    fun step(node: AstNode) {
        instructionCount++
        checkForTimeout()
    }

    fun allocate(size: Long = 1) {
        memoryCount += size
        checkForMemoryLimit()
    }

    fun free(size: Long = 1) {
        memoryCount -= size
    }

    private fun checkForTimeout() {
        if (instructionCount >= instructionLimit) {
            throw SchemeError(
                "Function exceeded timeout",
                "The execution of the Interpreter was cancelled because it exceeded the timeout limit of $instructionLimit instructions",
                null,
                null,
            )
        }
    }

    private fun checkForMemoryLimit() {
        if (memoryCount >= memoryLimit) {
            throw SchemeError(
                "Function exceeded memory limit",
                "The execution of the Interpreter was cancelled because it exceeded the memory limit of $memoryLimit",
                null,
                null,
            )
        }
    }
}
