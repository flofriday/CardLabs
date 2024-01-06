import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import simulation.Config
import simulation.Simulation
import simulation.models.Bot
import simulation.models.SimulationRequest
import simulation.models.SimulationResult
import java.text.SimpleDateFormat

val REQUEST_QUEUE = "match-queue"
val RESULT_QUEUE = "match-result-queue"

fun main(args: Array<String>) {
    val config = Config()
    config.load()

    val factory = ConnectionFactory()
    factory.host = config.rmqHost
    if (config.rmqUser != null) {
        factory.setUsername(config.rmqUser)
    }
    if (config.rmqPassword != null) {
        factory.setPassword(config.rmqPassword)
    }
    if (config.rmqVirtualHost != null) {
        factory.setVirtualHost("sypfwnyo")
    }

    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.queueDeclare(REQUEST_QUEUE, true, false, false, null)
    channel.queueDeclare(RESULT_QUEUE, true, false, false, null)

    val mapper = buildJsonMapper()
    val deliverCallback =
        DeliverCallback { consumerTag: String?, delivery: Delivery ->
            println("FLOOOO")
            val message = String(delivery.body, charset("UTF-8"))
            val request = mapper.readValue(message, SimulationRequest::class.java)
            val result = runGame(request)
            channel.basicPublish("", RESULT_QUEUE, null, mapper.writeValueAsBytes(result))
        }

    println("Waiting for requests...")
    channel.basicConsume(REQUEST_QUEUE, true, deliverCallback) { tag -> }
}

fun test() {
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
    val req = SimulationRequest(42, listOf(bot1, bot2))
    println(buildJsonMapper().writeValueAsString(req))
}

fun runGame(request: SimulationRequest): SimulationResult {
    println("Simulating game ${request.gameId}")
    val simulation = Simulation(request.gameId, request.participatingBots)
    println("Finished game ${request.gameId}")
    return simulation.run()
}

fun buildJsonMapper(): ObjectMapper {
    return ObjectMapper()
        .registerKotlinModule()
        .registerModule(JavaTimeModule()) // To support Java 8 time classes
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Disable writing dates as timestamps
        .setSerializationInclusion(JsonInclude.Include.NON_NULL) // Exclude null values when serializing
        .setDateFormat(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"),
        ); // Convert the date in RFC3339 format (https://www.rfc-editor.org/rfc/rfc3339)
}
