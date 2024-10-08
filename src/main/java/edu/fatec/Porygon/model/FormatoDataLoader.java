package edu.fatec.Porygon.model;

import edu.fatec.Porygon.repository.FormatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FormatoDataLoader implements CommandLineRunner {

    @Autowired
    private FormatoRepository formatoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (formatoRepository.count() == 0) {
            Formato json = new Formato();
            json.setNome("JSON");

            Formato csv = new Formato();
            csv.setNome("CSV");

            Formato xml = new Formato();
            xml.setNome("XML");

            formatoRepository.save(json);
            formatoRepository.save(csv);
            formatoRepository.save(xml);

            System.out.println("Formatos padrão inseridos no banco de dados.");
        }
    }
}
