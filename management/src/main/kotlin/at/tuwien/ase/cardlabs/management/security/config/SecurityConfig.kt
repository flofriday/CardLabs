package at.tuwien.ase.cardlabs.management.security.config

import at.tuwien.ase.cardlabs.management.security.DatabaseUserDetailsService
import at.tuwien.ase.cardlabs.management.security.jwt.JwtAuthenticationFilter
import at.tuwien.ase.cardlabs.management.security.jwt.JwtTokenService
import at.tuwien.ase.cardlabs.management.service.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!production")
class SecurityConfig(
    private val accountService: AccountService,
    private val jwtTokenService: JwtTokenService,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { csrf ->
                csrf.disable()
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(AntPathRequestMatcher("/oauth2")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/authentication/refresh")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/locations")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/leaderboard/public")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/leaderboard/firstPlace")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/account", "POST")).permitAll()
                    .requestMatchers(AntPathRequestMatcher("/account/open", "GET")).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .addFilterBefore(
                JwtAuthenticationFilter(DatabaseUserDetailsService(accountService), jwtTokenService),
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder,
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        authenticationProvider.setPasswordEncoder(passwordEncoder)

        return ProviderManager(authenticationProvider)
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        return DatabaseUserDetailsService(accountService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // when changing the password encoder to something different, it will break all passwords stored in the database
        return BCryptPasswordEncoder()
    }
}
