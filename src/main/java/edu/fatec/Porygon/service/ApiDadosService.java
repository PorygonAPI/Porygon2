package edu.fatec.Porygon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;

@Service
public class ApiDadosService {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    public Page<ApiDados> listarApiDados(Pageable pageable) {
        return apiDadosRepository.findAll(pageable);
    }
    
}