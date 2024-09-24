package edu.fatec.Porygon;

import edu.fatec.Porygon.service.DataScrapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.*;

@SpringBootApplication
public class PorygonApplication {

	public static void main(String[] args) {
		run(PorygonApplication.class, args);
	}

}
