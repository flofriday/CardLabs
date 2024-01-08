
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
        try {
            SchemeInterpreter().run(program)
            exitProcess(0)
        } catch (e: SchemeError) {
            e.display(program)
            exitProcess(1)
        }
    }

    println("The CardLabs Scheme Interpreter")
    println("Made with ❤️ by CardLabs")
    var interpreter = SchemeInterpreter()
    while (true) {
        print(">>> ")
        var program = readln()
        if (program.trim().isEmpty()) continue

        try {
            var tokens = Tokenizer().tokenize(program)
            while (tokens.filter { t -> t is LParenToken }.size > tokens.filter { t -> t is RParenToken }.size) {
                print("... ")
                val newLine = readln()
                program += "\n" + newLine
                tokens = Tokenizer().tokenize(program)
            }

            val obj = interpreter.run(program)
            if (obj != null && obj !is VoidValue) {
                println(obj)
            }
        } catch (e: SchemeError) {
            println()
            e.display(program)
        }
    }
}