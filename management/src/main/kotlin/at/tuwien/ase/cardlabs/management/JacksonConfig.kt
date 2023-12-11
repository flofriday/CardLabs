package at.tuwien.ase.cardlabs.management

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return jacksonObjectMapper().apply {
            registerKotlinModule() // Register Kotlin module
            registerModule(JavaTimeModule()) // To support Java 8 time classes
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Disable writing dates as timestamps
        }
    }
}
