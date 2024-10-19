package edu.fatec.Porygon.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;

@Service
public class NoticiaService {
    
    @Autowired
    private NoticiaRepository noticiaRepository;

    public List<Noticia> listarNoticias() {
        return noticiaRepository.findAll();
    }

    public List<Noticia> listarNoticiasPorData(LocalDate dataInicio, LocalDate dataFim) {
         return noticiaRepository.searchNewsByData(dataInicio, dataFim);
    }
        
    public Page<Noticia> listarNoticias(Pageable pageable) {
        return noticiaRepository.findAll(pageable);
    }

    public Optional<Noticia> findById(Integer id) {
        return noticiaRepository.findById(id); 
    }
}