import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.rabbitmq.client.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import simulation.Config
import simulation.models.SimulationRequest
import simulation.runSimulation
import java.io.IOException
import java.net.ConnectException
import java.text.SimpleDateFormat

val REQUEST_QUEUE = "match-queue"
val RESULT_QUEUE = "match-result-queue"

fun main(args: Array<String>) {
    val logger: Logger = LoggerFactory.getLogger("Main")

    logger.info("Loading config")
    val config = Config()
    config.load()

    logger.info("Connecting to RabbitMQ")
    val factory = ConnectionFactory()
    factory.host = config.rmqHost
    factory.setRequestedHeartbeat(10)
    factory.isAutomaticRecoveryEnabled = true
    factory.setNetworkRecoveryInterval(5)
    if (config.rmqPort != null) {
        factory.port = config.rmqPort!!
    }
    if (config.rmqUser != null) {
        factory.username = config.rmqUser
    }
    if (config.rmqPassword != null) {
        factory.password = config.rmqPassword
    }
    if (config.rmqVirtualHost != null) {
        factory.virtualHost = config.rmqVirtualHost
    }

    // Try connecting till rabbitMQ is reachable
    var connection: Connection? = null
    while (true) {
        try {
            connection = factory.newConnection()
            break
        } catch (e: ConnectException) {
            logger.warn("Unable to connect to RabbitMQ")
            Thread.sleep(5000)
        } catch (e: IOException) {
            logger.warn("Unable to connect to RabbitMQ")
            Thread.sleep(5000)
        }
    }

    val channel = connection!!.createChannel()
    channel.queueDeclare(REQUEST_QUEUE, true, false, false, null)
    channel.queueDeclare(RESULT_QUEUE, true, false, false, null)

    val mapper = buildJsonMapper()
    val deliverCallback =
        DeliverCallback { consumerTag: String?, delivery: Delivery ->
            val message = String(delivery.body, charset("UTF-8"))
            val request = mapper.readValue(message, SimulationRequest::class.java)
            logger.info("Simulating game ${request.gameId}...")
            val result = runSimulation(request)
            logger.info("Finished game ${request.gameId}")
            channel.basicPublish("", RESULT_QUEUE, null, mapper.writeValueAsBytes(result))
        }

    logger.info("Waiting for requests...")
    channel.basicConsume(REQUEST_QUEUE, true, deliverCallback) { tag -> }
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
