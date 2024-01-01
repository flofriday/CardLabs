package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.amqp.MatchQueue
import at.tuwien.ase.cardlabs.management.amqp.MatchResultQueue
import com.github.fridujo.rabbitmq.mock.compatibility.MockConnectionFactoryFactory
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestRabbitMQBeanConfig {

    @Value("\${cardlabs.rabbitmq.queue.match-queue}")
    private val matchQueue: String? = null

    @Value("\${cardlabs.rabbitmq.queue.match-result-queue}")
    private val matchResultQueue: String? = null

    @Bean
    fun connectionFactory(): ConnectionFactory {
        return CachingConnectionFactory(MockConnectionFactoryFactory.build().enableConsistentHashPlugin())
    }

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        jackson2JsonMessageConverter: Jackson2JsonMessageConverter
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
}
