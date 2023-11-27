data class SchemeError(val header: String, val reason: String, val location: Location?, val tip: String?) :
    Throwable() {
    fun display(program: String) {
        val RESET = "\u001b[0m"
        val RED = "\u001b[31m"
        val GREEN = "\u001b[32m"
        val BLUE = "\u001b[34m"
        val CYAN = "\u001b[36m"
        val GREY = "\u001b[37m"

        print(CYAN)
        println("-- " + header.uppercase() + " " + "-".repeat(80 - 2 - header.length))
        println(RESET)
        println(reason)
        println()
        if (location != null) {
            if (location.startline == location.endline) {
                val line = program.lines().get(location.startline - 1)
                print(GREY)
                print("%03d| ".format(location.startline) + line)
                println(RESET)
                println(" ".repeat(5 + location.startcol - 1) + RED + "^".repeat(location.endcol + 1 - location.startcol) + RESET)
            } else {
                print(GREY)
                println(
                    program.lines()
                        .drop(location.startline - 1)
                        .take(location.endline - location.startline + 1)
                        .zip(location.startline..location.endline)
                        .joinToString("\n") { (l, n) -> "%03d| ".format(n) + RED + "> " + GREY + l },
                )
                print(RESET)
            }
        } else {
            println("    No preview available")
        }
        println()
        if (tip != null) {
            println(GREEN + "TIP: " + RESET + tip)
        }
    }
}
