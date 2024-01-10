package at.tuwien.ase.cardlabs.management.amqp

import at.tuwien.ase.cardlabs.management.matchmaker.MatchQueueMessage
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener

class MatchQueueListener(
    private val objectMapper: ObjectMapper
) : MessageListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onMessage(message: Message) {
        val msg = objectMapper.readValue(message.body, object : TypeReference<MatchQueueMessage>() {})
        logger.debug("Received message '$msg'")
    }
}
