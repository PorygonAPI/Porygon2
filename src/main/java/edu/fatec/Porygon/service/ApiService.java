package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import edu.fatec.Porygon.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    public List<Api> listarTodas() {
        return apiRepository.findAll();
    }

    public Optional<Api> buscarPorId(Integer id) {
        return apiRepository.findById(id);
    }

    public String salvarOuAtualizar(Api api) {

        if (api.getUrl() == null || api.getUrl().contains(" ")) {
            throw new IllegalArgumentException("A URL não pode ser nula e não pode conter espaços.");
        }

        if (apiRepository.existsByNome(api.getNome())) {
            throw new IllegalArgumentException("Já existe uma API cadastrada com este nome.");
        }
        
        if (apiRepository.existsByUrl(api.getUrl())) {
            throw new IllegalArgumentException("Já existe uma API cadastrada com esta URL.");
        }
    
        Api savedApi;
        String message;
    
        try {
            if (api.getId() == null) {
                api.setDataCriacao(LocalDate.now());
                savedApi = apiRepository.save(api);
    
                if (savedApi.isAtivo()) {
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> response = restTemplate.getForEntity(savedApi.getUrl(), String.class);
    
                    ApiDados apiDados = new ApiDados();
                    apiDados.setConteudo(response.getBody());
                    apiDados.setDescricao("Dados da API: " + savedApi.getNome());
                    apiDados.setApi(savedApi);
                    apiDadosRepository.save(apiDados);
    
                    savedApi.setUltimaAtualizacao(LocalDate.now());
                    apiRepository.save(savedApi);
    
                    message = "Cadastro de portal e primeira coleta de notícias realizados com sucesso!";
                } else {
                    message = "Cadastro de portal realizado, mas sem coleta de notícias pois o portal está desativado.";
                }
            } else {
                Api apiExistente = apiRepository.findById(api.getId())
                        .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + api.getId()));
                api.setDataCriacao(apiExistente.getDataCriacao());
                api.setUltimaAtualizacao(apiExistente.getUltimaAtualizacao());
                savedApi = apiRepository.save(api);
    
                message = "Atualização de API realizada com sucesso.";
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar a API: " + e.getMessage());
        }
    
        return message; 
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