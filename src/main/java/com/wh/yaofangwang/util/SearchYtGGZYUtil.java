package com.wh.yaofangwang.util;

import com.wh.yaofangwang.common.Constants;
import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.model.bid.BidModel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SearchBzGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/20
 */
public class SearchYtGGZYUtil {

    static String GGZY_YT_URL = "http://ggzyjy.yantai.gov.cn/search_pageIndex_param.jspx?q=search_param";

    public static LinkedHashMap<String, List<String>> searchYTGgzyLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZY_YT_URL.replace("search_param", str);
            System.out.println("+++++++++++++++++++++:" + tempUrl);
            List<String> oldHrefs = null;
            if (resultMap != null)
                oldHrefs = resultMap.get(str);
            try {
                boolean flag = true;
                int count = 1;
                while (flag){
                    //获取超链接
                    String pageUrl = tempUrl.replace("pageIndex_param", String.valueOf(count));
                    System.out.println("pageUrl:"+pageUrl);
                    Document doc = DataCamling.docCamling(pageUrl);
                    if (doc.getElementsByClass("article-list2").size() <= 0) flag = false;
                    Elements resultTile = doc.getElementsByClass("article-list2");
                    if (resultTile.size() <= 0) flag = false;
                    Elements aList = resultTile.get(0).select("a");
                    if (aList == null || aList.size() <= 0) flag = false;
                    for (Element a :aList) {
                        String href = a.attr("href");
                        if (hrefs.contains(href)) flag = false;
                        if (oldHrefs != null && oldHrefs.contains(href)) {flag = false; break;}
                        String title = a.text();
                        if (title.contains("中标") || title.contains("招标")
                                || title.contains("成交") || title.contains("合同"))
                            hrefs.add(href);
                    }
                    count ++;
                    pageUrl = null;
                }
                map.put(str, hrefs);
                tempUrl = null;
                hrefs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    public static List<BidModel> searchYTGgzyDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Element content = document.getElementById("content");
                    if (content != null) {
                        String title = document.getElementsByClass("content-title").get(0).text();
                        bidModel.setTitle(title);
                        String html = content.html();
                        bidModel.setContent(html);
                        list.add(bidModel);
                        html = null;
                        title = null;
                        bidModel = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        return list;
    }


}
