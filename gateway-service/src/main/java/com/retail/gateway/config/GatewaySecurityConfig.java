package com.retail.gateway.config;

import com.retail.gateway.security.JwtAuthenticationEntryPoint;
import com.retail.gateway.security.JwtAuthenticationManager;
import com.retail.gateway.security.JwtServerAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class GatewaySecurityConfig {

  private final JwtAuthenticationManager authenticationManager;
  private final JwtServerAuthenticationConverter authenticationConverter;
  private final JwtAuthenticationEntryPoint authenticationEntryPoint;

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(authenticationManager);
    jwtFilter.setServerAuthenticationConverter(authenticationConverter);
    jwtFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));
    jwtFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(authenticationEntryPoint));

    http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .authorizeExchange(ex -> ex
            .pathMatchers("/auth/**", "/eureka/**", "/actuator/**").permitAll()
            .pathMatchers("/api/members/signup", "/api/members/login").permitAll()
            .anyExchange().authenticated()
        )
        .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION);

    return http.build();
  }
}
