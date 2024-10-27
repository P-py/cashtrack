package com.pedrosant.cashtrack.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CorsConfigurationProperties::class)
class SecurityConfiguration(
    private val authenticationProvider:AuthenticationProvider,
    private val corsProperties: CorsConfigurationProperties,
) {
    @Bean
    fun securityFilterChain(
        http:HttpSecurity,
        jwtAuthenticationFilter:JwtAuthenticationFilter
    ):DefaultSecurityFilterChain =
        http
            .cors { it.configurationSource(corsConfig()) }
            .csrf { it.disable() }
            .authorizeHttpRequests{
                it
                    .requestMatchers(
                        "/auth",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-ui/index.html",
                        "/v3/api-docs/**",
                        "/hello"
                    )
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/users", "/expenses/admin-list", "/incomes/admin-list")
                    .hasRole("ADMIN")
                    .requestMatchers(
                        HttpMethod.GET,
                        "/users/account",
                        "/users/balance",
                        "/incomes/{id}",
                        "/incomes",
                        "/expenses/{id}",
                        "/expenses")
                    .fullyAuthenticated()
                    .requestMatchers(HttpMethod.POST, "/users")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/incomes", "/expenses")
                    .fullyAuthenticated()
                    .requestMatchers(HttpMethod.PUT, "/users", "/incomes", "/expenses")
                    .fullyAuthenticated()
                    .requestMatchers(
                        HttpMethod.DELETE,
                        "/users/delete-my-account",
                        "/incomes/{id}",
                        "/expenses/{id}")
                    .fullyAuthenticated()
            }
            .sessionManagement{
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun corsConfig(): CorsConfigurationSource {
        val allowedOrigins = corsProperties.allowedOriginPatterns.split(";").map { it.trim() }
        val allowedMethods = listOf(
            HttpMethod.OPTIONS.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.PUT.name(),
            HttpMethod.GET.name(),
            HttpMethod.HEAD.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.POST.name(),
        )

        val configuration = CorsConfiguration()
        configuration.addAllowedHeader("*")
        configuration.addExposedHeader("Cookie")
        configuration.allowedOriginPatterns = allowedOrigins
        configuration.allowedMethods = allowedMethods
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)

        return source
    }
}