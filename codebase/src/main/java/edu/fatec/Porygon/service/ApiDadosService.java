package edu.fatec.Porygon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;

public class ApiDadosService {

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    public List<ApiDados> listarApiDados() {
        return apiDadosRepository.findAll();
    }
    
}