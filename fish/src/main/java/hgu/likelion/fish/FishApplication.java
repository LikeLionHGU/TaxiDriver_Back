package hgu.likelion.fish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FishApplication {

	public static void main(String[] args) {
		SpringApplication.run(FishApplication.class, args);
	}

}
