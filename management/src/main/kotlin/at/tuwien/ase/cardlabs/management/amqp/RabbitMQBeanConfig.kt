package at.tuwien.ase.cardlabs.management.amqp

import at.tuwien.ase.cardlabs.management.mapper.GameMapper
import at.tuwien.ase.cardlabs.management.service.bot.BotService
import at.tuwien.ase.cardlabs.management.service.game.GameService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("!test")
class RabbitMQBeanConfig {
    @Value("\${spring.rabbitmq.host}")
    private val host: String? = null

    @Value("\${spring.rabbitmq.port}")
    private val port = 0

    @Value("\${spring.rabbitmq.username}")
    private val username: String? = null

    @Value("\${spring.rabbitmq.password}")
    private val password: String? = null

    @Value("\${spring.rabbitmq.virtual-host}")
    private val virtualHost: String? = null

    @Value("\${cardlabs.rabbitmq.queue.match-queue}")
    private val matchQueue: String? = null

    @Value("\${cardlabs.rabbitmq.queue.match-result-queue}")
    private val matchResultQueue: String? = null

    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.setHost(host!!)
        connectionFactory.port = port
        connectionFactory.username = username!!
        connectionFactory.setPassword(password!!)

        if (virtualHost != null) {
            connectionFactory.virtualHost = virtualHost
        }

        connectionFactory.setRequestedHeartBeat(30)
        connectionFactory.setConnectionTimeout(30000)

        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        jackson2JsonMessageConverter: Jackson2JsonMessageConverter,
    ): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jackson2JsonMessageConverter
        return rabbitTemplate
    }

    @Bean
    @MatchQueue
    fun matchQueue(): Queue {
        return Queue(matchQueue!!)
    }

    @Bean
    @MatchResultQueue
    fun matchResultQueue(): Queue {
        return Queue(matchResultQueue!!)
    }

    @Bean
    fun listenerContainerMatchResultQueueMessage(
        connectionFactory: ConnectionFactory,
        objectMapper: ObjectMapper,
        gameService: GameService,
        gameMapper: GameMapper,
        botService: BotService,
    ): SimpleMessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.acknowledgeMode = AcknowledgeMode.AUTO
        container.setQueueNames(matchResultQueue)
        container.setMessageListener(
            MatchResultQueueListener(
                objectMapper,
                gameService,
                gameMapper,
                botService,
            ),
        )
        return container
    }
}
