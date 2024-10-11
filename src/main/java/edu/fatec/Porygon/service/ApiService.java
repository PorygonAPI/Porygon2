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

    public Api salvarOuAtualizar(Api api) {
        boolean isNew = (api.getId() == null);

        if (isNew) {
            if (apiRepository.existsByNome(api.getNome())) {
                throw new IllegalArgumentException("Já existe uma API cadastrada com este nome.");
            }
            if (apiRepository.existsByUrl(api.getUrl())) {
                throw new IllegalArgumentException("Já existe uma API cadastrada com esta URL.");
            }
            api.setDataCriacao(LocalDate.now());
            Api savedApi = apiRepository.save(api);

            if (savedApi.isAtivo()) {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<String> response = restTemplate.getForEntity(savedApi.getUrl(), String.class);

                ApiDados apiDados = new ApiDados();
                apiDados.setConteudo(response.getBody());
                apiDados.setDescricao("Dados da API: " + savedApi.getNome());
                apiDados.setApi(savedApi);

                apiDadosRepository.save(apiDados);

                savedApi.setUltimaAtualizacao(LocalDate.now());
                savedApi = apiRepository.save(savedApi);
            }

            return savedApi;
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
            return apiRepository.save(api);
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