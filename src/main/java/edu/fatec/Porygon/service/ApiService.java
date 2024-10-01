package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    public List<Api> listarTodas() {
        return apiRepository.findAll();
    }

    public Optional<Api> buscarPorId(Integer id) {
        return apiRepository.findById(id);
    }

    public Api salvarOuAtualizar(Api api) {
        if (apiRepository.existsByNome(api.getNome())) {
            throw new IllegalArgumentException("Já existe uma API cadastrada com este nome.");
        }
        if (apiRepository.existsByUrl(api.getUrl())) {
            throw new IllegalArgumentException("Já existe uma API cadastrada com esta URL.");
        }
        if (api.getId() == null) {
            api.setDataCriacao(LocalDate.now());
        } else {
            Api apiExistente = apiRepository.findById(api.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + api.getId()));
            api.setDataCriacao(apiExistente.getDataCriacao());
        } 
        return apiRepository.save(api);
    }

    public Api alterarStatus(Integer id, boolean novoStatus) {
        Optional<Api> apiOptional = apiRepository.findById(id);
        if (apiOptional.isPresent()) {
            Api api = apiOptional.get();
            api.setAtivo(novoStatus);
            return apiRepository.save(api);
        }
        return null;
    }

}
