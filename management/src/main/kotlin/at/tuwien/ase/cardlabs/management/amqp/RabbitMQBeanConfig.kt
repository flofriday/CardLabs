package at.tuwien.ase.cardlabs.management.amqp

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
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
        connectionFactory.virtualHost = virtualHost!!

        connectionFactory.setRequestedHeartBeat(30)
        connectionFactory.setConnectionTimeout(30000)

        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        return RabbitTemplate(connectionFactory)
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
}