import cardscheme.LParenToken
import cardscheme.RParenToken
import cardscheme.SchemeError
import cardscheme.SchemeInterpreter
import cardscheme.Tokenizer
import cardscheme.VoidValue
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size > 1) {
        println("usage: cardscheme [FILE]")
        exitProcess(1)
    }

    if (args.size == 1) {
        val program = File(args.get(0)).readText()
        val startime = System.currentTimeMillis()
        try {
            SchemeInterpreter().run(program)
        } catch (e: SchemeError) {
            e.display(program)
            exitProcess(1)
        }
        println("Executed in: ${System.currentTimeMillis() - startime}ms")
        exitProcess(0)
    }

    println("The CardScheme Interpreter")
    println("Made with ❤️ by CardLabs")
    var lineNr = 1
    var completeProgram = ""
    val interpreter = SchemeInterpreter()
    while (true) {
        print(">>> ")
        var program = readln()
        if (program.trim().isEmpty()) continue

        try {
            lineNr++
            var tokens = Tokenizer().tokenize("\n".repeat(lineNr - 2) + program)
            while (tokens.filter { t -> t is LParenToken }.size > tokens.filter { t -> t is RParenToken }.size) {
                print("... ")
                val newLine = readln()
                program += "\n" + newLine
                lineNr++
                tokens = Tokenizer().tokenize("\n".repeat(lineNr - 3) + program)
            }

            if (completeProgram.isEmpty()) {
                completeProgram = program
            } else {
                completeProgram += "\n" + program
            }
            val obj = interpreter.run(tokens)
            if (obj != null && obj !is VoidValue) {
                println(obj)
            }
        } catch (e: SchemeError) {
            e.display(completeProgram)
        }
    }
}
