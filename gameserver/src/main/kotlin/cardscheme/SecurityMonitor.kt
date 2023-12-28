package cardscheme

class SecurityMonitor(private val instructionLimit: Long) {
    private var instructionCount = 0
    fun step(node: AstNode) {
        instructionCount++
        checkForTimeout()
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
}
