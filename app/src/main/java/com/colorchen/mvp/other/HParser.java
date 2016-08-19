package com.colorchen.mvp.other;

import android.content.Context;
import android.util.Log;

import com.colorchen.mvp.model.HDetail;
import com.colorchen.mvp.model.HItem;
import com.colorchen.mvp.model.Image;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.RealmList;


/**
 * H post parser.
 */
public class HParser {

    private Context context;
    private int type;

    public HParser(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public static List<HItem> parseHItem(String response) {
        List<HItem> items = new ArrayList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.select(".tr3 > td > h3 > a");
        for (int i = 3; i < elements.size(); i++) {
            Element post = elements.get(i);
            String url = post.attr("abs:href");
            String title = post.text();
            HItem item = new HItem(System.currentTimeMillis() + "", url, title);
            items.add(item);
            Log.i("test", "href>>>" + url + " text>>>" + title);
        }
        return items;
    }

    public HDetail parseHDetail(final String response, final String url) {
        Log.i("test", " Start parse HDetail>>>");
        RealmList<Image> images = new RealmList<>();
        Document document = Jsoup.parse(response);
        Elements elements = document.select(".tpc_content > img[src]");
        for (int i = 0; i < elements.size(); i++) {
            Element img = elements.get(i);
            String src = img.attr("src");
            try {
                Image image = Image.getFixedImage(context, src, type);
                images.add(image);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new HDetail(url, images);
    }

}
