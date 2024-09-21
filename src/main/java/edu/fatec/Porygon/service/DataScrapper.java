package edu.fatec.Porygon.service;

import edu.fatec.Porygon.model.Noticia;
import edu.fatec.Porygon.repository.NoticiaRepository;
import edu.fatec.Porygon.repository.PortalRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DataScrapper {

    @Autowired
    private NoticiaRepository noticiaRepository;
    @Autowired
    private PortalRepository portalRepository;


    public List<Noticia> teste(){ return noticiaRepository.findAll(); };



    public void scrapeData(String url){

       try{
           //Criando a conexão como site por meio do Jsoup
           Document doc = Jsoup.connect(url).get();

           //Criando a estrutura para iterar a página


           //Criando a estrutura para salvar os elementos da notícia
           Element Titulo, Data, Autor;
           Elements conteudo;


           //Salvar os arquivos no banco de dados


       } catch (IOException e) {
           throw new RuntimeException(e);
       }

    }

}
