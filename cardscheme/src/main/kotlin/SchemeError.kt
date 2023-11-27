data class SchemeError(val header: String, val reason: String, val location: Location?, val tip: String?) :
    Throwable() {
    fun display(program: String) {
        val reset = "\u001b[0m"
        val red = "\u001b[31m"
        val green = "\u001b[32m"
        val blue = "\u001b[34m"
        val cyan = "\u001b[36m"
        val grey = "\u001b[37m"

        print(cyan)
        println("-- " + header.uppercase() + " " + "-".repeat(80 - 2 - header.length))
        println(reset)
        println(reason)
        println()
        if (location != null) {
            if (location.startline == location.endline) {
                val line = program.lines().get(location.startline - 1)
                print(grey)
                print("%03d| ".format(location.startline) + line)
                println(reset)
                println(" ".repeat(5 + location.startcol - 1) + red + "^".repeat(location.endcol + 1 - location.startcol) + reset)
            } else {
                print(grey)
                println(
                    program.lines()
                        .drop(location.startline - 1)
                        .take(location.endline - location.startline + 1)
                        .zip(location.startline..location.endline)
                        .joinToString("\n") { (l, n) -> "%03d| ".format(n) + red + "> " + grey + l },
                )
                print(reset)
            }
        } else {
            println("    No preview available")
        }
        println()
        if (tip != null) {
            println(green + "TIP: " + reset + tip)
        }
    }
}
