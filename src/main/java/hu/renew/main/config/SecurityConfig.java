package hu.renew.main.config;

import hu.renew.main.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // JWT miatt nincs session és CSRF
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // 🔓 Nyilvános frontend fájlok
                .requestMatchers(
                        "/", "/index.html",
                        "/css/**", "/js/**", "/img/**", "/fonts/**", "/favicon.ico",
                        "/store/**", "/contact/**", "/auth/**", "/messages/**", "/about/**"
                ).permitAll()

                // 🔓 Auth végpontok
                .requestMatchers("/auth/register", "/auth/login").permitAll()

                // 🔓 Nyilvános API-k (termékek, OS, processzor)
                .requestMatchers(HttpMethod.GET,
                        "/api/processor/**",
                        "/api/os/**",
                        "/api/laptops/**"
                ).permitAll()

                // 🔓 Üzenet küldése engedélyezett
                .requestMatchers(HttpMethod.POST, "/api/contact").permitAll()

                // 🔒 Üzenetek lekérése (csak bejelentkezett felhasználónak)
                .requestMatchers(HttpMethod.GET, "/api/contact").authenticated()

                // 🔒 Minden más védett
                .anyRequest().authenticated()
            )

            // JWT filter beszúrása
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}

