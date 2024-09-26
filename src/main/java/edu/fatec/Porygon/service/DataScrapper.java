package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.repository.AgendadorRepository;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class DataScrapper {

    @Autowired
    private NoticiaRepository noticiaRepository;
    @Autowired
    private PortalRepository portalRepository;
    @Autowired
    private AgendadorRepository agendadorRepository;

    public void scrapeDatabyPortalID(int id){
        //Selecionando o portal desejado pelo id
        Portal portal = portalRepository.findById(id).orElseThrow(() -> new RuntimeException("Portal not found with id: " + id));

        //Selecionando a classe que contém as notícias na página principal
        String classNameLink = portal.getSeletorCaminhoNoticia();
        String referenceClass = "a[href]." + classNameLink;

        //Extraindo a url
        String url = portal.getUrl();

        try{
            //Criando a estrutura para salvar os elementos da notícia
            List<Noticia> noticias = new ArrayList<>();
            Element Titulo, Data, Autor;
            Elements Conteudo;

            //Criando a conexão com o site por meio do Jsoup
            Document doc = Jsoup.connect(url).get();

            //Criando a estrutura para iterar a página
            Elements selectPag = doc.select(referenceClass);
            HashSet<String> Links = new HashSet<>();
            for (Element selector : selectPag) {
                Links.add(selector.absUrl("href"));
            }

            // Uma vez extraido o array de links, iterar cada um deles e retirar as informações
            for (String link: Links) {
                Document linkDoc = Jsoup.connect(link).get();
                Titulo = linkDoc.select(portal.getSeletorTitulo()).first();
                Data = linkDoc.select(portal.getSeletorDataPublicacao()).first();
                Autor = linkDoc.select(portal.getSeletorJornalista()).first();
                Conteudo=linkDoc.select(portal.getSeletorConteudo());

                // Manipulação da Data
                String verificationDate = "";
                // Verificação se há conteudo na variável
                if (Data != null) {
                    // Caso possua, retira o valor do atributo "datetime"
                    verificationDate = Data.attr("datetime");
                } else {
                    // Caso não possua, insere uma data genérica para indicar o erro
                    System.err.println("No date element found for link: " + link);
                    verificationDate = "1001-01-01T10:44:13.253Z";
                }

                //Converter a data manipulada para formato aceito no banco
                Date dataPublicacao = convertStringToDate(verificationDate);

                //Manipulação do conteúdo
                StringBuilder contentScrapperBuilder = new StringBuilder();
                for (Element conteudo: Conteudo){
                    contentScrapperBuilder.append(conteudo.text()).append("\n");
                };
                String contentScrapper = contentScrapperBuilder.toString();

                //Criando a estrutura da notícia p/salvar no banco
                Noticia noticia = new Noticia();
                noticia.setTitulo(Titulo != null ? Titulo.text() : "Titulo Desconhecido");
                noticia.setAutor(Autor != null ? Autor.text() : "Autor Desconhecido");
                noticia.setData(dataPublicacao);
                noticia.setPortal(portal);
                noticia.setConteudo(contentScrapper);

                //Adicionar todas as notícias à lista
                noticias.add(noticia);
            }

            // salvar a lista no BD
            noticiaRepository.saveAll(noticias);

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

    @Scheduled(fixedRate = 1800000) // Executa a função a cada meia hora de modo automático
    public void WebScrapper() {
        List<Portal> portais = portalRepository.findAll();

        for (Portal portal : portais) {
            if (portal.isAtivo()) {
                scrapeDatabyPortalID(portal.getId());
            }
        }

    }

}
