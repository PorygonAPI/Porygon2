package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import jakarta.annotation.PostConstruct;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DataScrapperService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    @Autowired
    private PortalRepository portalRepository;

    public void scrapeDatabyPortalID(int id) {
        Portal portal = portalRepository.findById(id).orElseThrow(() -> new RuntimeException("Portal not found with id: " + id));

        String classNameLink = portal.getSeletorCaminhoNoticia();
        String referenceClass = "a[href]." + classNameLink;

        String url = portal.getUrl();

        try {
            List<Noticia> noticias = new ArrayList<>();
            Element Titulo, Data, Autor;
            Elements Conteudo;

            Document doc = Jsoup.connect(url).get();
            Elements selectPag = doc.select(referenceClass);
            HashSet<String> Links = new HashSet<>();
            for (Element selector : selectPag) {
                Links.add(selector.absUrl("href"));
            }

            for (String link : Links) {
                Document linkDoc = Jsoup.connect(link).get();
                Titulo = linkDoc.select(portal.getSeletorTitulo()).first();
                Data = linkDoc.select(portal.getSeletorDataPublicacao()).first();
                Autor = linkDoc.select(portal.getSeletorJornalista()).first();
                Conteudo = linkDoc.select(portal.getSeletorConteudo());

                String verificationDate = "";
                if (Data != null) {
                    verificationDate = Data.attr("datetime");
                } else {
                    System.err.println("No date element found for link: " + link);
                    verificationDate = "1001-01-01T10:44:13.253Z";
                }

                Date dataPublicacao = convertStringToDate(verificationDate);

                StringBuilder contentScrapperBuilder = new StringBuilder();
                for (Element conteudo: Conteudo){
                    contentScrapperBuilder.append(conteudo.text()).append("\n");
                };
                String contentScrapper = contentScrapperBuilder.toString();

                Noticia noticia = new Noticia();
                noticia.setTitulo(Titulo != null ? Titulo.text() : "Titulo Desconhecido");
                noticia.setAutor(Autor != null ? Autor.text() : "Autor Desconhecido");
                noticia.setData(dataPublicacao);
                noticia.setPortal(portal);
                noticia.setConteudo(contentScrapper);
                noticia.setHref(link);

                // Verifica se a notícia já existe no banco
                if (!noticiaRepository.existsByHref(noticia.getHref())) {
                    noticias.add(noticia); // Adiciona à lista se não existir
                } else {
                    System.out.println("Notícia já existente: " + noticia.getHref());
                }
            }

            // Salva as notícias não duplicadas
            if (!noticias.isEmpty()) {
                noticiaRepository.saveAll(noticias);
            }

            portal.setHasScrapedToday(true);
            portal.setUltimaAtualizacao(LocalDate.now());
            portalRepository.save(portal);

        } catch (IOException e) {
            System.err.println("Error fetching URL: " + url + " - " + e.getMessage());
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

    public void WebScrapper() {
        showLoading(); // Exibe o loading no início

        List<Portal> portais = portalRepository.findAll();

        for (Portal portal : portais) {
            if(!portal.isHasScrapedToday()) {
                if (portal.isAtivo()) {
                    scrapeDatabyPortalID(portal.getId());
                }
            }
        }

        hideLoading(); // Oculta o loading no final
    }

    // Métodos para exibir e ocultar a tela de carregamento
    private void showLoading() {
        // Aqui você pode implementar a lógica para mostrar a tela de carregamento
        System.out.println("Carregamento iniciado...");
    }

    private void hideLoading() {
        // Lógica para ocultar a tela de carregamento
        System.out.println("Carregamento encerrado.");
    }

    //Set to run at noon(12h00)
    @Scheduled(cron = "0 0 12 * * *")
    public void WebScrapingScheduledDate(){
        List<Portal> portais = portalRepository.findAll();
        for (Portal portal : portais) {

            int updateRate = portal.getAgendador().getQuantidade();

            if (updateRate == 1){

                if(!portal.isHasScrapedToday() && portal.isAtivo()) {
                    scrapeDatabyPortalID(portal.getId());
                }

            } else if (updateRate == 7 || updateRate==30) {
                if(!portal.isHasScrapedToday() && portal.isAtivo()) {

                    LocalDate today = LocalDate.now();
                    LocalDate lastUpdate = portal.getUltimaAtualizacao();
                    int daysBetween = (int) ChronoUnit.DAYS.between(lastUpdate, today);

                    if(daysBetween>=updateRate){
                        scrapeDatabyPortalID(portal.getId());
                    }
                }
            }

        }
    }

    //Set to run every day at 23h50
    @Scheduled(cron = "0 50 23 * * *")
    public void ResetHasScrapedToday(){
        List<Portal> portals = portalRepository.findAll();
        List<Portal> resetList = new ArrayList<>();
        for (Portal portal : portals) {
            portal.setHasScrapedToday(false);
            resetList.add(portal);
        }
        portalRepository.saveAll(resetList);
    }

    //runs the function once after the DataScrappedService is created/activated
    @PostConstruct
    public void resetScrapedTodayVerifiedStartProgram(){
        List<Portal> portals = portalRepository.findAll();
        List<Portal> resetList = new ArrayList<>();
        for (Portal portal : portals) {
            if(!Objects.equals(portal.getUltimaAtualizacao(), LocalDate.now())) {
                portal.setHasScrapedToday(false);
                resetList.add(portal);
            }
        }
        portalRepository.saveAll(resetList);
    }

    //Runs the webscraping when the program starts
    @PostConstruct
    public void WebscrapingWhenStart(){
        List<Portal> portais = portalRepository.findAll();
        for (Portal portal : portais) {

            int updateRate = portal.getAgendador().getQuantidade();

            if (updateRate == 1){

                if(!portal.isHasScrapedToday() && portal.isAtivo()) {
                    scrapeDatabyPortalID(portal.getId());
                }

            } else if (updateRate == 7 || updateRate==30) {
                if(!portal.isHasScrapedToday() && portal.isAtivo()) {

                    LocalDate today = LocalDate.now();
                    LocalDate lastUpdate = portal.getUltimaAtualizacao();
                    int daysBetween = (int) ChronoUnit.DAYS.between(lastUpdate, today);

                    if(daysBetween>=updateRate){
                        scrapeDatabyPortalID(portal.getId());
                    }
                }
            }

        }
    }

}
