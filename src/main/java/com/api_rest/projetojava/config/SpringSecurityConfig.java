package com.api_rest.projetojava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import com.api_rest.projetojava.config.SwaggerConfig;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    protected SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomBasicAuthFilter customBasicAuthFilter) throws Exception {

        http
            .authorizeHttpRequests(
                    authorizeConfig -> {
                        authorizeConfig.requestMatchers("/consultas/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                        authorizeConfig.anyRequest().authenticated();
                    }
            )
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(customBasicAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf().disable();

        return http.build();
    }

}