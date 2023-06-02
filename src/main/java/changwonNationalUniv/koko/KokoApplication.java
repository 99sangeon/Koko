package changwonNationalUniv.koko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KokoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KokoApplication.class, args);
	}

}
