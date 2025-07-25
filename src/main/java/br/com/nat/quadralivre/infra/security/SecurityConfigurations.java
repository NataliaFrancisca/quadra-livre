package br.com.nat.quadralivre.infra.security;

import br.com.nat.quadralivre.infra.expection.CustomAccessDeniedHandler;
import br.com.nat.quadralivre.infra.expection.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/registrar").permitAll()

                        .requestMatchers(HttpMethod.GET, "/quadras/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/quadras/**/horario-funcionamento").authenticated()

                        .requestMatchers("/quadras/***").hasRole("GESTOR")

                        .requestMatchers("/reservas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/reservas").hasRole("CLIENTE")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> {
                        ex.accessDeniedHandler(new CustomAccessDeniedHandler());
                        ex.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                    }
                )
                .addFilterBefore(this.securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}