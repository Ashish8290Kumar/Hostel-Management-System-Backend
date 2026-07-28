package com.AshishWork.HostelManagementSystem.Configuration;

import com.AshishWork.HostelManagementSystem.JWT.JwtAuthenticationFilter;
import com.AshishWork.HostelManagementSystem.Security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.ERROR).permitAll()
                        .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.FORWARD).permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/", "/index.html", "/error", "/assets/**", "/favicon.ico").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/v1/documents/**").permitAll()

                        .requestMatchers("/api/payments/**").authenticated()
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/student/**").hasAnyAuthority("ROLE_STUDENT", "ROLE_ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
