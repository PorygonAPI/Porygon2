package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Agendador;
import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import edu.fatec.Porygon.repository.ApiRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.HashSet;

@Service
public class ApiRotinaService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @PostConstruct
    public void verificarApisNaInicializacao() {
        System.out.println("Verificando e atualizando APIs ao iniciar a aplicação...");
        verificarEAtualizarApis();
    }

    public void verificarEAtualizarApis() {
        List<Api> apis = apiRepository.findAll();

        for (Api api : apis) {
            if (api.isAtivo() && api.getAgendador() != null) {
                Agendador agendador = api.getAgendador();
                LocalDate hoje = LocalDate.now();
                LocalDate ultimaAtualizacao = api.getUltimaAtualizacao();

                int intervaloDias = agendador.getQuantidade();

                if (ultimaAtualizacao == null ||
                        hoje.isEqual(ultimaAtualizacao.plusDays(intervaloDias)) ||
                        hoje.isAfter(ultimaAtualizacao.plusDays(intervaloDias))) {
                    realizarRequisicaoApi(api);
                }
            }
        }
    }

    public void realizarRequisicaoApi(Api api) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(api.getUrl(), String.class);
            ApiDados apiDados = new ApiDados();
            apiDados.setConteudo(response.getBody());
            apiDados.setDescricao("Dados da API: " + api.getNome());
            apiDados.setApi(api);
            apiDados.setDataColeta(LocalDate.now());
            apiDados.setTags(new HashSet<>(api.getTags())); // Associar as tags da API aos ApiDados
            apiDadosRepository.save(apiDados);

            api.setUltimaAtualizacao(LocalDate.now());
            apiRepository.save(api);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao realizar a requisição para a API: " + e.getMessage());
        }
    }
}