package edu.fatec.Porygon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class NoticiaService {
    
    @Autowired
    private NoticiaRepository noticiaRepository;

    // Método para listar notícias paginadas
    public Page<Noticia> listarNoticias(Pageable pageable) {
        return noticiaRepository.findAll(pageable);
    }

    public Optional<Noticia> findById(Integer id) {
        return noticiaRepository.findById(id);  // Reutilizando o método do NoticiaRepository
    }

    /*private NoticiaRepository noticiaRepository;

    public List<Noticia> listarNoticias() {
        return noticiaRepository.findAll();
    }/* */
    
    // public List<Noticia> listarNoticiasPorData(java.util.Date dataInicio, java.util.Date dataFim) {
    //     return noticiaRepository.acharDataEntre(dataInicio, dataFim);
    // }
}
