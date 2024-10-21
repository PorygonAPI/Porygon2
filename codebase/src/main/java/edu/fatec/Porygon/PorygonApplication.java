package edu.fatec.Porygon;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
@EnableScheduling
public class PorygonApplication {

	public static void main(String[] args) {
		run(PorygonApplication.class, args);
	}

}
