package edu.fatec.Porygon.config;

import edu.fatec.Porygon.model.Agendador;
import edu.fatec.Porygon.repository.AgendadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AgendadorDataLoader implements CommandLineRunner {

    @Autowired
    private AgendadorRepository agendadorRepository;

    @Override
    public void run(String... args) throws Exception {
        if (agendadorRepository.count() == 0) {
            Agendador diario = new Agendador();
            diario.setTipo("Diário");
            diario.setQuantidade(1);

            Agendador semanal = new Agendador();
            semanal.setTipo("Semanal");
            semanal.setQuantidade(7);

            Agendador mensal = new Agendador();
            mensal.setTipo("Mensal");
            mensal.setQuantidade(30);

            agendadorRepository.save(diario);
            agendadorRepository.save(semanal);
            agendadorRepository.save(mensal);

        }
    }
}


