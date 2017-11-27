package com.homework.networm.service;

import com.homework.networm.dao.HrefTextMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CrawService {

    @Resource
    private HrefTextMapper hrefTextMapper;

    public void craw() {
        getUrlData("http://tech.sina.com.cn/");
    }

    // 提取数据的操作方法
    public void getUrlData(String urlStr) {
        try {
            Document doc = Jsoup.connect(urlStr).get();

            String title = doc.title();
            // 描述
            Elements allHref = doc.getElementsByTag("a");
            for (Element aEl : allHref) {
                // 取得里面的href属性
                String href = aEl.attr("href");
                if (href.startsWith("http://tech.sina.com.cn") && href.matches(".*\\d{4}-\\d{2}-\\d{2}.*\\d+\\.shtml")) {
                    String itemText = aEl.text();
                    hrefTextMapper.add(itemText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
