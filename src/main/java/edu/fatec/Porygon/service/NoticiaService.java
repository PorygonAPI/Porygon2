package edu.fatec.Porygon.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class NoticiaService {
    
    @Autowired
    private NoticiaRepository noticiaRepository;

    public Page<Noticia> listarNoticias(Pageable pageable) {
        return noticiaRepository.findAll(pageable);
    }

    public Page<Noticia> listarNoticiasPorData(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
         return noticiaRepository.searchNewsByData(dataInicio, dataFim, pageable);
    }
}