package at.tuwien.ase.cardlabs.management.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule()) // To support Java 8 time classes
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Disable writing dates as timestamps
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL) // Exclude null values when serializing
        mapper.setDateFormat(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")); // Convert the date in RFC3339 format (https://www.rfc-editor.org/rfc/rfc3339)
        return mapper
    }

    @Bean
    fun jackson2JsonMessageConverter(objectMapper: ObjectMapper): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter(objectMapper)
    }
}
