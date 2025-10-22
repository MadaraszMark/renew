package hu.renew.main.config;

import hu.renew.main.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.disable())
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth

				// 🔓 Nyilvános frontend fájlok (HTML, JS, CSS stb.)
				.requestMatchers("/", "/index.html", "/css/**", "/js/**", "/img/**", "/fonts/**",
						"/favicon.ico", "/store/**", "/contact/**", "/auth/**", "/messages/**",
						"/chart/**", "/about/**")
				.permitAll()

				// 🔓 Auth végpontok
				.requestMatchers("/auth/register", "/auth/login").permitAll()

				// 🔓 Publikus API-k (processzorok, OS, laptopok, diagram)
				.requestMatchers("/api/processors/**", "/api/os/**", "/api/laptops/**", "/api/chart/**")
				.permitAll()

				// 🔓 Kosár REST API (publikus elérés)
				.requestMatchers("/api/cart/**").permitAll()

				// 🔓 Üzenetküldés engedélyezett
				.requestMatchers(HttpMethod.POST, "/api/contact").permitAll()

				// 🔒 Üzenetek lekérése (bejelentkezett)
				.requestMatchers(HttpMethod.GET, "/api/contact").authenticated()

				// 🟢 az admin oldal statikusan betölthető
				.requestMatchers("/admin", "/admin/**", "/admin.html").permitAll()

				// 🔒 az admin API továbbra is csak ROLE_ADMIN
				.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

				// 🔒 Minden más védett
				.anyRequest().authenticated()
			)
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

