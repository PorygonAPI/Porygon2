package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.model.Jornalista;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DataScrapperService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private PortalRepository portalRepository;

    @Autowired
    private JornalistaService jornalistaService;

    @Autowired
    private NoticiaService noticiaService;

    @Transactional
    @Async
    public CompletableFuture<Void> scrapeDatabyPortalID(int id) {
        Portal portal = portalRepository.findByIdWithTags(id)
                .orElseThrow(() -> new RuntimeException("Portal not found with id: " + id));
        portal.getTags().size();
    
        String classNameLink = portal.getSeletorCaminhoNoticia();
        String referenceClass = "a[href]." + classNameLink;
        String url = portal.getUrl();
    
        try {
            Document doc = Jsoup.connect(url).get();
            Elements selectPag = doc.select(referenceClass);
            HashSet<String> Links = new HashSet<>();
            
            for (Element selector : selectPag) {
                Links.add(selector.absUrl("href"));
            }
    
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            for (String link : Links) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        processLink(link, portal);
                    } catch (IOException e) {
                        System.err.println("Erro ao processar o link: " + link + " - " + e.getMessage());
                    }
                });
                futures.add(future);
            }
    
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            portal.setHasScrapedToday(true);
            portal.setUltimaAtualizacao(LocalDate.now());
            portalRepository.save(portal);
    
        } catch (IOException e) {
            System.err.println("Error fetching URL: " + url + " - " + e.getMessage());
        }
    
        return CompletableFuture.completedFuture(null);
    }
    
    private void processLink(String link, Portal portal) throws IOException {
        Document linkDoc = Jsoup.connect(link).get();
        Element Titulo = linkDoc.select(portal.getSeletorTitulo()).first();
        Element Data = linkDoc.select(portal.getSeletorDataPublicacao()).first();
        Element Autor = linkDoc.select(portal.getSeletorJornalista()).first();
        Elements Conteudo = linkDoc.select(portal.getSeletorConteudo());
    
        String verificationDate = (Data != null) ? Data.attr("datetime") : "1001-01-01T10:44:13.253Z";
        Date dataPublicacao = convertStringToDate(verificationDate);
    
        StringBuilder contentScrapperBuilder = new StringBuilder();
        for (Element conteudo : Conteudo) {
            contentScrapperBuilder.append(conteudo.text()).append("\n");
        }
        String contentScrapper = contentScrapperBuilder.toString();
    
        if (Titulo == null || Titulo.text().trim().isEmpty()
                || Autor == null || Autor.text().trim().isEmpty()
                || contentScrapper.trim().isEmpty()) {
            return;  
        }
    
        Jornalista jornalista = jornalistaService.obterOuCriarJornalista(Autor.text());
    

        Noticia noticia = new Noticia();
        noticia.setTitulo(Titulo.text());
        noticia.setJornalista(jornalista);
        noticia.setData(dataPublicacao);
        noticia.setPortal(portal);
        noticia.setConteudo(contentScrapper);
        noticia.setHref(link);
    
        if (!noticiaRepository.existsByHref(noticia.getHref())) {
            noticiaService.salvar(noticia);
        }
    }
    

    public CompletableFuture<Document> fetchDocumentAsync(String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new RuntimeException("Error fetching URL: " + url, e);
            }
        });
    }

    @Async
    public CompletableFuture<Void> WebScrapper() {
        showLoading();

        List<Portal> portais = portalRepository.findAll();

        CompletableFuture<Void> allScrapingTasks = CompletableFuture.allOf(
            portais.stream()
                   .filter(portal -> !portal.isHasScrapedToday() && portal.isAtivo())
                   .map(portal -> scrapeDatabyPortalID(portal.getId()))
                   .toArray(CompletableFuture[]::new)
        );

        allScrapingTasks.join();

        hideLoading();
        return CompletableFuture.completedFuture(null);
    }

    public void validarSeletores(Portal portal) {
        try {
            Document doc = Jsoup.connect(portal.getUrl()).get();
    
            String seletorCaminhoNoticia = "a[href]." + portal.getSeletorCaminhoNoticia();
            Element firstLinkElement = doc.select(seletorCaminhoNoticia).first();
    
            if (firstLinkElement == null) {
                throw new IllegalArgumentException("O seletor do caminho de notícia está inválido. Verifique!");
            }
    
            String noticiaUrl = firstLinkElement.absUrl("href");
            //System.out.println("Validando seletores com a notícia encontrada: " + noticiaUrl);

            Document noticiaDoc = Jsoup.connect(noticiaUrl).get();
    
            if (noticiaDoc.select(portal.getSeletorTitulo()).isEmpty()) {
                throw new IllegalArgumentException("O seletor de título está inválido. Verifique!");
            }
    
            if (noticiaDoc.select(portal.getSeletorDataPublicacao()).isEmpty()) {
                throw new IllegalArgumentException("O seletor de data de publicação está inválido. Verifique!");
            }
    
            if (noticiaDoc.select(portal.getSeletorJornalista()).isEmpty()) {
                throw new IllegalArgumentException("O seletor de jornalista está inválido. Verifique!");
            }
    
            if (noticiaDoc.select(portal.getSeletorConteudo()).isEmpty()) {
                throw new IllegalArgumentException("O seletor de conteúdo está inválido. Verifique!");
            }
    
            //System.out.println("Todos os seletores foram validados com sucesso para o portal: " + portal.getNome());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao acessar a URL: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            //System.err.println("Erro de validação de seletores: " + e.getMessage());
            throw e;
        }
    }
    

    private Date convertStringToDate(String dateStr) {
        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, DateTimeFormatter.ISO_DATE_TIME);
            Instant instant = zonedDateTime.toInstant();
            return Date.from(instant);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse date: " + dateStr, e);
        }
    }

    private void showLoading() {
        //System.out.println("Carregamento iniciado...");
    }

    private void hideLoading() {
        //System.out.println("Carregamento encerrado.");
    }

    // Set to run at noon(12h00)
    @Scheduled(cron = "0 0 12 * * *")
    public void WebScrapingScheduledDate() {
        List<Portal> portais = portalRepository.findAll();
        for (Portal portal : portais) {

            int updateRate = portal.getAgendador().getQuantidade();

            if (updateRate == 1) {

                if (!portal.isHasScrapedToday() && portal.isAtivo()) {
                    scrapeDatabyPortalID(portal.getId());
                }

            } else if (updateRate == 7 || updateRate == 30) {
                if (!portal.isHasScrapedToday() && portal.isAtivo()) {

                    LocalDate today = LocalDate.now();
                    LocalDate lastUpdate = portal.getUltimaAtualizacao();
                    int daysBetween = (int) ChronoUnit.DAYS.between(lastUpdate, today);

                    if (daysBetween >= updateRate) {
                        scrapeDatabyPortalID(portal.getId());
                    }
                }
            }
        }
    }

    // Set to run every day at 23h50
    @Scheduled(cron = "0 50 23 * * *")
    public void ResetHasScrapedToday() {
        List<Portal> portals = portalRepository.findAll();
        List<Portal> resetList = new ArrayList<>();
        for (Portal portal : portals) {
            portal.setHasScrapedToday(false);
            resetList.add(portal);
        }
        portalRepository.saveAll(resetList);
    }

    @PostConstruct
    public void resetScrapedTodayVerifiedStartProgram() {
        List<Portal> portals = portalRepository.findAll();
        List<Portal> resetList = new ArrayList<>();
        for (Portal portal : portals) {
            if (!Objects.equals(portal.getUltimaAtualizacao(), LocalDate.now())) {
                portal.setHasScrapedToday(false);
                resetList.add(portal);
            }
        }
        portalRepository.saveAll(resetList);
    }

    @PostConstruct
    public void WebscrapingWhenStart() {
        List<Portal> portais = portalRepository.findAll();
        for (Portal portal : portais) {

            int updateRate = portal.getAgendador().getQuantidade();

            if (updateRate == 1) {

                if (!portal.isHasScrapedToday() && portal.isAtivo()) {
                    scrapeDatabyPortalID(portal.getId());
                }

            } else if (updateRate == 7 || updateRate == 30) {
                if (!portal.isHasScrapedToday() && portal.isAtivo()) {

                    LocalDate today = LocalDate.now();
                    LocalDate lastUpdate = portal.getUltimaAtualizacao();

                    if (lastUpdate == null) {
                        //System.err.println("lastUpdate is null for portal: " + portal.getId());
                        continue;
                    }

                    int daysBetween = (int) ChronoUnit.DAYS.between(lastUpdate, today);

                    if (daysBetween >= updateRate) {
                        scrapeDatabyPortalID(portal.getId());
                    }
                }
            }
        }
    }

}