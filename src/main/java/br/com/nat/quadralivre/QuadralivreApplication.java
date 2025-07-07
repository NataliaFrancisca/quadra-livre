package br.com.nat.quadralivre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QuadralivreApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuadralivreApplication.class, args);
	}

}
