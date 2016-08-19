package com.colorchen.mvp.other;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by color on 16/4/26 16:56.
 */
public class DBParser {
    public static String[] parse(String response) {
        Document document = Jsoup.parse(response);
        Elements elements = document.select("div[class=thumbnail] > div[class=img_single] > a > img");
        final int size = elements.size();
        String[] urls = new String[size];
        for (int i = 0; i < size; i++) {
            urls[i] = elements.get(i).attr("src");
        }
        return urls;
    }
}
