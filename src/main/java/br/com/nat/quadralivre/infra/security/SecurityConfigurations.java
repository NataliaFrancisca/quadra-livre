package br.com.nat.quadralivre.infra.security;

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
                        .requestMatchers(HttpMethod.POST, "/login/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll()
                        .requestMatchers("/usuarios/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/quadras").hasAnyRole("GESTOR", "USUARIO")
                        .requestMatchers(HttpMethod.GET, "/quadras/*").hasAnyRole("GESTOR", "USUARIO")

                        .requestMatchers(HttpMethod.POST, "/quadras/**").hasRole("GESTOR")
                        .requestMatchers(HttpMethod.DELETE, "/quadras/**").hasRole("GESTOR")
                        .requestMatchers(HttpMethod.PUT, "/quadras/**").hasRole("GESTOR")


                        .requestMatchers("/reservas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/reservas").hasRole("CLIENTE")
                        .anyRequest().authenticated()
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