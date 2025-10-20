package hu.renew.main.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()   // minden elérhető
            )
            .csrf(csrf -> csrf.disable())   // CSRF védelem kikapcsolása fejlesztéshez
            .formLogin(login -> login.disable()) // login oldal kikapcsolása
            .httpBasic(basic -> basic.disable()); // HTTP basic auth kikapcsolása

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }

}