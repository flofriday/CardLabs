import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import simulation.models.Bot
import simulation.Simulation

fun main(args: Array<String>) {
    val bot1 =
        Bot(
            1,
            1,
            """
            (define (turn topCard hand players)
                (random-choice
                    (matching-cards topCard hand)))
            """.trimIndent(),
        )

    val bot2 =
        Bot(
            2,
            2,
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

    val mapper = JsonMapper.builder().addModule(JavaTimeModule()).build()
    println(mapper.writeValueAsString(result))

}
