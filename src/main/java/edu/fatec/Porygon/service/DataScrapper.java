package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.model.Portal;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class DataScrapper {

    @Autowired
    private NoticiaRepository noticiaRepository;
    @Autowired
    private PortalRepository portalRepository;

    //teste - conexão,iterar pág. principal e raspagem com base no BD

    // Teste de conexão
//    public String testConnection(String url) {
//        try {
//            Document doc = Jsoup.connect(url).get();
//            return doc.text();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Teste p/retirada dos links da página principal
//    public List<String> testParseMainPage(String url) {
//        try{
//            Document doc = Jsoup.connect(url).get();
//            Elements elm = doc.select("a");
//            ArrayList<String> Links = new ArrayList<>();
//            for (Element el : elm) {
//                Links.add(el.attr("href"));
//            }
//            return Links;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // Teste p/ fazer o Web Scrapping com base nos portais do BD
//    public List<String> ScrapperTestPortals(){
//        // Lista para armazenar os títulos das páginas
//        List<String> titulos = new ArrayList<>();
//
//        //Acessar os portais salvos no banco e iterar todas as urls encontradas
//        List<Portal> portais = portalRepository.findAll();
//
//        for (Portal portal : portais) {
//            String url = portal.getUrl();
//            try {
//                // Conectando-se ao site via Jsoup
//                Document doc = Jsoup.connect(url).get();
//
//                // Exemplo de scraping para pegar o título da página (ajuste os seletores conforme necessário)
//                Element titulo = doc.selectFirst("meta[property=og:title]");
//
//                // Verifique se o título foi encontrado
//                if (titulo != null) {
//                    // Adicionar o título à lista
//                    titulos.add(titulo.attr("content"));
//                } else {
//                    // Se o título não for encontrado, adicione uma string indicando isso
//                    titulos.add("Título não encontrado para URL: " + url);
//                }
//
//            } catch (Exception e) {
//                // Adicionar uma string de erro à lista se algo der errado
//                titulos.add("Erro ao acessar a URL: " + url + " - Erro: " + e.getMessage());
//            }
//        }
//
//        // Retornar a lista de títulos
//        return titulos;
//    }

    // Web Scrapping - funções

    public void scrapeDatabyPortalID(int id){
        //Selecionando o portal desejado pelo id
        Portal portal = portalRepository.findById(id).orElseThrow(() -> new RuntimeException("Portal not found with id: " + id));

        //Selecionando a classe que contém as notícias na página principal
        String classNameLink = portal.getCaminhoNoticia();
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
                Titulo = linkDoc.select(portal.getTitulo()).first();
                Data = linkDoc.select(portal.getDataPublicacao()).first();
                Autor = linkDoc.select(portal.getJornalista()).first();
                Conteudo=linkDoc.select(portal.getConteudo());

                //Converter a data para formato aceito no banco
                LocalDateTime dataPublicacao = convertStringToLocalDateTime(Data.attr("datetime"), "dd-MM-yyyy");

                //Criando a estrutura da notícia p/salvar no banco
                Noticia noticia = new Noticia();
                noticia.setTitulo(Titulo != null ? Titulo.text() : "Titulo Desconhecido");
                noticia.setAutor(Autor != null ? Autor.text() : "Autor Desconhecido");
                noticia.setData(dataPublicacao);
                noticia.setPortal(portal);

                //Adicionar todas as notícias à lista
                noticias.add(noticia);
            }

            // salvar a lista no BD
            noticiaRepository.saveAll(noticias);

        } catch (IOException e) {
            System.err.println("Error fetching URL: " + url + " - " + e.getMessage());
        }

    }

    private LocalDateTime convertStringToLocalDateTime(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, formatter);
    }

}
