package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.fatec.Porygon.model.Jornalista;
import edu.fatec.Porygon.repository.JornalistaRepository;
import java.util.Optional;

@Service
public class JornalistaService {

    @Autowired
    private JornalistaRepository jornalistaRepository;

    @Transactional
    public Jornalista salvarOuAtualizarJornalista(String nomeAutor) {
        if (nomeAutor.toLowerCase().startsWith("por ")) {
            nomeAutor = nomeAutor.substring(4).trim();
        }

        Optional<Jornalista> jornalistaExistente = jornalistaRepository.findByNome(nomeAutor);

        if (jornalistaExistente.isPresent()) {
            return jornalistaExistente.get();
        }

        Jornalista novoJornalista = new Jornalista();
        novoJornalista.setNome(nomeAutor);
        return jornalistaRepository.save(novoJornalista);
    }
}