data class SchemeError(val header: String, val reason: String, val location: Location?, val tip: String?) :
    Throwable() {
    fun display(program: String) {
        println(header.uppercase())
        println("-".repeat(80))
        println()
        println(reason)
        println()
        if (location != null) {
            if (location.startline == location.endline) {
                val line = program.lines().get(location.startline - 1)
                println("%03d| ".format(location.startline) + line)
                println(" ".repeat(5 + location.startcol - 1) + "^".repeat(location.endcol + 1 - location.startcol))
            } else {
                println("    Multiline preview not implemented")
            }
        } else {
            println("    No preview available")
        }
        println()
        if (tip != null) {
            println(tip)
        }
    }
}
