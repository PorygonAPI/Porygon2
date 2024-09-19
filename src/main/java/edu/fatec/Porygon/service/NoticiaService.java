package edu.fatec.Porygon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;

@Service
public class NoticiaService {
    @Autowired
    private NoticiaRepository noticiaRepository;

    public List<Noticia> listasNoticias() {
        return noticiaRepository.findAll();
    }    
}
