package edu.fatec.Porygon;

import edu.fatec.Porygon.service.DataScrapperService;
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
