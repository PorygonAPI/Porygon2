package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import edu.fatec.Porygon.repository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.HttpServerErrorException;
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
        boolean isNew = (api.getId() == null);
    
        if (api.getUrl() == null || api.getUrl().contains(" ")) {
            throw new IllegalArgumentException("A URL não pode conter espaços.");
        }
    
        if (isNew) {
            if (apiRepository.existsByNome(api.getNome())) {
                throw new IllegalArgumentException("Já existe uma API cadastrada com este nome.");
            }
            if (apiRepository.existsByUrl(api.getUrl())) {
                throw new IllegalArgumentException("Já existe uma API cadastrada com esta URL.");
            }
            api.setDataCriacao(LocalDate.now());
        } else {
            Api apiExistente = apiRepository.findById(api.getId())
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + api.getId()));
    
            if (!apiExistente.getNome().equals(api.getNome()) && apiRepository.existsByNome(api.getNome())) {
                throw new IllegalArgumentException("Já existe uma API cadastrada com este nome.");
            }
            if (!apiExistente.getUrl().equals(api.getUrl()) && apiRepository.existsByUrl(api.getUrl())) {
                throw new IllegalArgumentException("Já existe uma API cadastrada com esta URL.");
            }

            api.setDataCriacao(apiExistente.getDataCriacao());
            api.setUltimaAtualizacao(LocalDate.now());
        }
    
        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getForEntity(api.getUrl(), String.class);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw new IllegalArgumentException("A URL fornecida é inválida ou não possui um endpoint válido! ");
            } catch (ResourceAccessException e) {
                throw new IllegalArgumentException("Não foi possível acessar a URL fornecida. Verifique se ela está ativa: " + e.getMessage());
            }
    
            Api savedApi = apiRepository.save(api);
    
            if (isNew && savedApi.isAtivo()) {
                RestTemplate restTemplateForData = new RestTemplate();
                ResponseEntity<String> response = restTemplateForData.getForEntity(savedApi.getUrl(), String.class);
    
                ApiDados apiDados = new ApiDados();
                apiDados.setConteudo(response.getBody());
                apiDados.setDescricao("Dados da API: " + savedApi.getNome());
                apiDados.setApi(savedApi);
                apiDados.setDataColeta(LocalDate.now());
    
                apiDadosRepository.save(apiDados);
    
                savedApi.setUltimaAtualizacao(LocalDate.now());
                apiRepository.save(savedApi);
    
                return "Cadastro de API e coleta REST realizada com sucesso!";
            } else if (isNew) {
                return "Cadastro de API realizado, mas sem coleta REST pois a API está desativada.";
            } else {
                return "Atualização de API realizada com sucesso.";
            }
    
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar a API: " + e.getMessage());
        }
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