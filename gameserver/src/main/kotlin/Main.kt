import simulation.models.Bot
import simulation.Simulation

fun main(args: Array<String>) {
    val bot1 =
        Bot(
            1,
            "Random Bot",
            """
            (define (turn topCard hand players)
                (random-choice
                    (matching-cards topCard hand)))
            """.trimIndent(),
        )

    val bot2 =
        Bot(
            2,
            "Random Display Bot",
            """
            (define (turn topCard hand players)
                (display players)
                (newline)
                (random-choice
                    (matching-cards topCard hand)))
            """.trimIndent(),
        )

    val simulation = Simulation(0, listOf(bot1, bot2))
    val result = simulation.run()
    println(result)
}
