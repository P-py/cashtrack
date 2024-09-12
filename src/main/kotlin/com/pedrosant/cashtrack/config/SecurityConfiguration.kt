package com.pedrosant.cashtrack.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val authenticationProvider:AuthenticationProvider
){
    @Bean
    fun securityFilterChain(
        http:HttpSecurity,
        jwtAuthenticationFilter:JwtAuthenticationFilter
    ):DefaultSecurityFilterChain =
        http
            .csrf{ it.disable() }
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
}