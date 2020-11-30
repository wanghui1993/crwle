package com.wh.yaofangwang.util;

import com.wh.yaofangwang.common.Constants;
import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.model.bid.BidModel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SearchGgzyjyzxUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/15
 */
public class SearchWhGGZYUtil {

    static String GGZY_WH_ROOT_URL = "http://www.whggzyjy.cn/";
    static String GGZY_WH_URL = "http://www.whggzyjy.cn/queryContent_pageIndex_param-jyxx.jspx?" +
            "title=search_param&inDates=&ext=&origin=&channelId=channelId_param&" +
            "beginTime=startDate_param&endTime=endDate_param";

    public static LinkedHashMap<String, LinkedHashSet<String>> searchWHGGZYLoop(int bidType, Map<String, List<String>> resultMap){
        //参数拼接 时间
        Map<String, String> dateMap = DateUtil.calaDate(bidType, "-");
        String startTime = dateMap.get("startTime");
        String endTime = dateMap.get("endTime");
        GGZY_WH_URL = GGZY_WH_URL.replace("startDate_param", startTime);
        GGZY_WH_URL = GGZY_WH_URL.replace("endDate_param", endTime);

        LinkedHashMap<String, LinkedHashSet<String>> map = new LinkedHashMap<>();
        Arrays.stream(Constants.params).forEach(str -> {
            LinkedHashSet<String> hrefs = new LinkedHashSet<>();
            //替换查询条件
            String tempUrl = GGZY_WH_URL.replace("search_param", str);
            //查询类型
            if (bidType == 1) tempUrl = tempUrl.replace("channelId_param", "86");
            if (bidType == 7) tempUrl = tempUrl.replace("channelId_param", "87");
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
                    if (resultTile.size() > 0) {
                        if (resultTile.get(0).select("a").size() > 0) {
                            Elements aList = resultTile.get(0).select("a");
                            for (Element a : aList) {
                                String href = a.attr("href");
                                href = GGZY_WH_ROOT_URL + href;
                                if (oldHrefs != null && oldHrefs.contains(href)) {flag = false; break;}
                                if (hrefs.contains(href)){
                                    flag = false;
//                                    continue;
                                } else
                                    hrefs.add(href);
                            }
                        } else {
                            flag = false;
                        }
                    } else {
                        flag = false;
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

    public static List<BidModel> searchWHGGZYDetail(LinkedHashMap<String, LinkedHashSet<String>> maps){
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
                        //标题
                        String title = content.getElementsByClass("content-title").get(0).text();
                        bidModel.setTitle(title);
                        //时间
                        String time = content.getElementById("time").text();
                        bidModel.setTime(time.substring(3));
                        //第一种页面格式
                        Elements section1 = content.getElementsByClass("Section1");
                        if (!CollectionUtils.isEmpty(section1)) {
                            Element element = section1.get(0);
                            String html = element.html();
                            bidModel.setContent(html);
                            html = null;
                        } else {
                            Element content1 = content.getElementById("content");
                            String html = content1.html();
                            bidModel.setContent(html);
                            html = null;
                        }
                        list.add(bidModel);
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
