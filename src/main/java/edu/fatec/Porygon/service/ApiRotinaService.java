package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Agendador;
import edu.fatec.Porygon.model.Api;
import edu.fatec.Porygon.model.ApiDados;
import edu.fatec.Porygon.repository.ApiDadosRepository;
import edu.fatec.Porygon.repository.ApiRepository;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.HashSet;
import java.util.logging.Logger;

@Service
public class ApiRotinaService {

    private static final Logger logger = Logger.getLogger(ApiRotinaService.class.getName());

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiDadosRepository apiDadosRepository;

    @PostConstruct
    public void verificarApisNaInicializacao() {
        logger.info("Verificando e atualizando APIs ao iniciar a aplicação...");
        verificarEAtualizarApis();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void verificarEAtualizarApis() {
        List<Api> apis = apiRepository.findAll();
        LocalDate hoje = LocalDate.now();

        for (Api api : apis) {
            if (api.isAtivo() && api.getAgendador() != null) {
                Agendador agendador = api.getAgendador();
                LocalDate ultimaAtualizacao = api.getUltimaAtualizacao();
                int intervaloDias = agendador.getQuantidade();

                if (ultimaAtualizacao == null ||
                        hoje.isEqual(ultimaAtualizacao.plusDays(intervaloDias)) ||
                        hoje.isAfter(ultimaAtualizacao.plusDays(intervaloDias))) {
                    logger.info("Iniciando atualização para API: " + api.getNome());
                    realizarRequisicaoApi(api);
                } else {
                    logger.info("API " + api.getNome() + " ainda não necessita de atualização.");
                }
            } else {
                logger.info("API " + api.getNome() + " está inativa ou sem agendamento configurado.");
            }
        }
    }

    public void realizarRequisicaoApi(Api api) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(api.getUrl(), String.class);
            String conteudo = response.getBody();

            api.setUltimaAtualizacao(LocalDate.now());
            apiRepository.save(api);

            if (apiDadosRepository.existsByConteudo(conteudo)) {
                logger.info("Conteúdo já existe no banco de dados para API: " + api.getNome());
                return;
            }

            ApiDados apiDados = new ApiDados();
            apiDados.setConteudo(conteudo);
            apiDados.setDescricao("Dados da API: " + api.getNome());
            apiDados.setApi(api);
            apiDados.setDataColeta(LocalDate.now());
            apiDados.setTags(new HashSet<>(api.getTags()));
            apiDadosRepository.save(apiDados);

            logger.info("Dados salvos com sucesso para a API: " + api.getNome());

        } catch (Exception e) {
            logger.severe("Erro ao realizar a requisição para a API " + api.getNome() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao realizar a requisição para a API: " + e.getMessage());
        }
    }
}