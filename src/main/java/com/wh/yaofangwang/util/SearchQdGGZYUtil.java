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
 * @ClassName searchGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/15
 */
public class SearchQdGGZYUtil {

    static String GGZY_QD_ROOT_URL = "https://ggzy.qingdao.gov.cn/";
    static String GGZY_QD_URL = "https://ggzy.qingdao.gov.cn/Tradeinfo-GGGSList/1-1-2?" +
            "ProjectName=ProjectName_param&ArryCode=&Time=300&" +
            "ClassId=&ZBFlag=0&pageIndex=pageIndex_param";


    public static LinkedHashMap<String, List<String>> searchQdGgzyLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZY_QD_URL.replace("ProjectName_param", str);
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
                    if (doc.getElementsByClass("info_con").size() <= 0) flag = false;
                    Elements resultTile = doc.getElementsByClass("info_con");
                    if (resultTile.size() <= 0) flag = false;
                    Elements aList = resultTile.get(0).select("a");
                    if (aList == null || aList.size() <= 0) flag = false;
                    for (Element a :aList) {
                        String href = a.attr("href");
                        href = GGZY_QD_ROOT_URL + href;
                        if (hrefs.contains(href)) flag = false;
                        if (oldHrefs != null && oldHrefs.contains(href)) {flag = false; break;}
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

    public static List<BidModel> searchQdGgzyDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Element htmlTable = document.getElementById("htmlTable");
                    if (htmlTable != null) {
                        String title = null;
                        if (htmlTable.getElementsByTag("h1").size() > 0)
                            title = htmlTable.getElementsByTag("h1").get(0).text();
                        else if (htmlTable.getElementsByClass("tle").size() > 0)
                            title = htmlTable.getElementsByClass("tle").get(0).text();
                        bidModel.setTitle(title);
                        String html = htmlTable.html();
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
