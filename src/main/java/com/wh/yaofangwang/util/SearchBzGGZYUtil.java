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
 * @ClassName SearchBzGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/20
 */
public class SearchBzGGZYUtil {

    static String GGZY_BZ_ROOT_URL = "http://ggzyjy.binzhou.gov.cn/";
    static String GGZY_BZ_URL = "http://ggzyjy.binzhou.gov.cn/bzweb/showinfo/searchresult.aspx?" +
            "keyword=search_param&searchtype=title";

    static String VIEWSTATE =
            "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";

    public static LinkedHashMap<String, List<String>> searchBZGGZYLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("__VIEWSTATE", VIEWSTATE);
        params.put("__VIEWSTATEGENERATOR", "C1DA0EB1");
        params.put("__EVENTTARGET", "SearchResult1$Pager");
        params.put("SearchResult1$Pager_input", "1");
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            List<String> oldHrefs = null;
            if (resultMap != null)
                oldHrefs = resultMap.get(str);
            //替换查询条件
            String tempUrl = GGZY_BZ_URL.replace("search_param", str);
            System.out.println("+++++++++++++++++++++:" + tempUrl);
            try {
                boolean flag = true;
                int count = 1;
                while (flag){
                    //获取超链接
                    params.put("__EVENTARGUMENT", String.valueOf(count));
                    System.out.println("--------------------------:" + count);
                    Document post = DataCamling.post(tempUrl, params);
                    Element tdcontent = post.getElementById("tdcontent");
                    if (tdcontent != null) {
                        Elements aList = tdcontent.select("a");
                        if (aList == null || aList.size() <= 0) flag = false;
                        for (Element a :aList) {
                            String href = a.attr("href");
                            href = GGZY_BZ_ROOT_URL + href;
                            if (hrefs.contains(href)) flag = false;
                            if (oldHrefs != null && oldHrefs.contains(href)) {flag = false; break;}
                            if (a.text().contains("中标") || a.text().contains("招标")
                                    || a.text().contains("成交") || a.text().contains("合同"))
                                hrefs.add(href);
                        }
                    } else {
                        flag = false;
                    }
                    count ++;
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

    public static List<BidModel> searchBZGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    //标题
                    String title = document.getElementById("tdTitle").text();
                    bidModel.setTitle(title);
                    //时间
                    Elements times = document.getElementsByClass("article-nav");
                    if (!CollectionUtils.isEmpty(times)) {
                        String time = times.get(0).text();
                        bidModel.setTime(time.substring(7, 16));
                        time = null;
                    }
                    Element tdContent = document.getElementById("TDContent");
                    bidModel.setContent(tdContent.html());
                    list.add(bidModel);
                    title = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        return list;
    }

}
