package edu.fatec.Porygon.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagScrapperService {

    public static List<String> buscarSinonimos(String palavra) {
        try {
            String url = "https://www.dicio.com.br/" + palavra + "/";
            Document doc = Jsoup.connect(url).get();
            Elements sinonimos = doc.select("p.sinonimos");

            if (!sinonimos.isEmpty()) {
                String textoCompleto = sinonimos.first().text();
                int index = textoCompleto.indexOf("sinônimo de:");
                if (index != -1) {
                    String sinonimosTexto = textoCompleto.substring(index + "sinônimo de:".length()).trim();
                    return Arrays.stream(sinonimosTexto.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}