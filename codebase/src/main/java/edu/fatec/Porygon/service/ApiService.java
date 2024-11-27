package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.model.Tag;
import edu.fatec.Porygon.repository.ApiRepository;
import edu.fatec.Porygon.repository.TagRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiRotinaService apiRotinaService;

    @Autowired
    private TagRepository tagRepository;

    public List<Api> listarTodas() {
        return apiRepository.findAll();
    }

    public Optional<Api> buscarPorId(Integer id) {
        return apiRepository.findByIdWithTags(id);
    }

    public String salvarOuAtualizar(Api api, List<Integer> tagIds) {
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

        if (tagIds != null && !tagIds.isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(tagIds));
            api.setTags(tags);
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.getForEntity(api.getUrl(), String.class);
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw new IllegalArgumentException("A URL fornecida é inválida ou não possui um endpoint válido!");
            } catch (ResourceAccessException e) {
                throw new IllegalArgumentException(
                        "Não foi possível acessar a URL fornecida. Verifique se ela está ativa: " + e.getMessage());
            }

            Api savedApi = apiRepository.save(api);

            if (isNew && savedApi.isAtivo()) {

                apiRotinaService.realizarRequisicaoApi(savedApi);

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

    public String salvarOuAtualizar(Api api) {
        return salvarOuAtualizar(api, null);
    }

    @Async
    @Transactional
    public CompletableFuture<Void> alterarStatus(Integer id, boolean novoStatus) {
        Optional<Api> apiOptional = apiRepository.findById(id);
        if (apiOptional.isPresent()) {
            Api api = apiOptional.get();
            api.setAtivo(novoStatus);
            apiRepository.save(api);
    
            if (novoStatus) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        apiRotinaService.realizarRequisicaoApi(api);
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao realizar a raspagem ao ativar a API: " + e.getMessage());
                    }
                });
            }
        }
    
        return CompletableFuture.completedFuture(null);
    }
    

    public Api atualizarTags(Integer apiId, List<Integer> tagIds) {
        Api api = apiRepository.findById(apiId)
                .orElseThrow(() -> new RuntimeException("API não encontrada"));

        if (tagIds != null) {
            Set<Tag> novasTags = tagIds.isEmpty()
                    ? new HashSet<>()
                    : new HashSet<>(tagRepository.findAllById(tagIds));
            api.setTags(novasTags);
        }

        return apiRepository.save(api);
    }
}