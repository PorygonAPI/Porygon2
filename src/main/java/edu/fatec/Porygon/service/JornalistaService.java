package edu.fatec.Porygon.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Jornalista;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.JornalistaRepository;
import edu.fatec.Porygon.repository.NoticiaRepository;

@Service
public class JornalistaService {

    @Autowired
    private JornalistaRepository jornalistaRepository;

    @Autowired
    private NoticiaRepository noticiaRepository;

    public void salvarOuAtualizarJornalista(Noticia noticia) {
        String nomeAutor = noticia.getAutor();
        if (nomeAutor.toLowerCase().startsWith("por ")) {
            nomeAutor = nomeAutor.substring(4).trim(); // Remove "Por" e os espaços em branco
        }

        Optional<Jornalista> jornalistaExistente = jornalistaRepository.findByNome(nomeAutor);

        Jornalista jornalista;
        if (jornalistaExistente.isPresent()) {
            jornalista = jornalistaExistente.get();
        } else {
            jornalista = new Jornalista();
            jornalista.setNome(nomeAutor);
            jornalista = jornalistaRepository.save(jornalista); // Salva o jornalista antes de associá-lo
        }

        noticia.setJornalista(jornalista);
        noticiaRepository.save(noticia);
    }
}