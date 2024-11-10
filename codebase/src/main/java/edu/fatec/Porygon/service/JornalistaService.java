package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Jornalista;
import edu.fatec.Porygon.repository.JornalistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JornalistaService {

    @Autowired
    private JornalistaRepository jornalistaRepository;

    public Jornalista obterOuCriarJornalista(String nome) {
        Jornalista jornalista = jornalistaRepository.findByNome(nome);
        if (jornalista == null) {
            jornalista = new Jornalista();
            jornalista.setNome(nome);
            jornalista = jornalistaRepository.save(jornalista);
        }
        return jornalista;
    }
}