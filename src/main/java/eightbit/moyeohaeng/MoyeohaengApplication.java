package eightbit.moyeohaeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoyeohaengApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoyeohaengApplication.class, args);
	}

}
