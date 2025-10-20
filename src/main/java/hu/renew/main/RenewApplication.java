package hu.renew.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RenewApplication {

	public static void main(String[] args) {
		SpringApplication.run(RenewApplication.class, args);
	}

}
