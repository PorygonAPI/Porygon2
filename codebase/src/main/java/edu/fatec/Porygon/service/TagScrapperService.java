package edu.fatec.Porygon.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TagScrapperService {

    public static String buscarSinonimo(String palavra) {
        try {
            String url = "https://www.dicio.com.br/" + palavra + "/";
            Document doc = Jsoup.connect(url).get();
            Elements sinonimos = doc.select("p.sinonimos");

            if (!sinonimos.isEmpty()) {
                String textoCompleto = sinonimos.first().text();
                int index = textoCompleto.indexOf("sinônimo de:");
                if (index != -1) {
                    return textoCompleto.substring(index + "sinônimo de:".length()).trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
