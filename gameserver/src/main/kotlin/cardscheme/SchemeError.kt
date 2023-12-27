package cardscheme

data class SchemeError(val header: String, val reason: String, val location: Location?, val tip: String?) :
    Throwable() {
    fun format(program: String): String {
        val output = StringBuilder()
        val reset = "\u001b[0m"
        val red = "\u001b[31m"
        val green = "\u001b[32m"
        val blue = "\u001b[34m"
        val cyan = "\u001b[36m"
        val grey = "\u001b[37m"

        output.append(cyan)
        output.append("-- " + header.uppercase() + " " + "-".repeat(80 - 2 - header.length) + "\n")
        output.append(reset + "\n")
        output.append(reason + "\n")
        output.append("\n")
        if (location != null) {
            if (location.startline == location.endline) {
                val line = program.lines().get(location.startline - 1)
                output.append(grey)
                output.append("%03d| ".format(location.startline) + line)
                output.append(reset + "\n")
                output.append(
                    " ".repeat(5 + location.startcol - 1) + red + "^".repeat(
                        location.endcol + 1 - location
                            .startcol,
                    ) + reset + "\n",
                )
            } else {
                output.append(grey)
                output.append(
                    program.lines()
                        .drop(location.startline - 1)
                        .take(location.endline - location.startline + 1)
                        .zip(location.startline..location.endline)
                        .joinToString("\n") { (l, n) -> "%03d| ".format(n) + red + "> " + grey + l } + "\n",
                )
                output.append(reset)
            }
        } else {
            output.append("    No preview available\n")
        }
        output.append("\n")
        if (tip != null) {
            output.append(green + "TIP: " + reset + tip + "\n")
        }

        return output.toString()
    }
}
