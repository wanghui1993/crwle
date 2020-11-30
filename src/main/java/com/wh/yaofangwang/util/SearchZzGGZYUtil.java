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
public class SearchZzGGZYUtil {

    static String GGZY_ZZ_ROOT_URL = "http://www.zzggzy.com/";
    static String GGZY_ZZ_URL = "http://www.zzggzy.com/TPFront/showinfo/searchresult.aspx?" +
            "keyword=search_param&searchtype=title";

    static String VIEWSTATE =
            "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";

    public static LinkedHashMap<String, List<String>> searchZZGGZYLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("__VIEWSTATE", VIEWSTATE);
        params.put("__VIEWSTATEGENERATOR", "1EAF79DA");
        params.put("__EVENTTARGET", "SearchResult1$Pager");
        params.put("__VIEWSTATEENCRYPTED", "");
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZY_ZZ_URL.replace("search_param", str);
            System.out.println("+++++++++++++++++++++:" + tempUrl);
            List<String> oldHrefs = null;
            if (resultMap != null)
                oldHrefs = resultMap.get(str);
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
                            href = GGZY_ZZ_ROOT_URL + href;
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

    public static List<BidModel> searchZZGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Element tdTitle = document.getElementById("tdTitle");
                    //标题
                    String title = tdTitle.getElementsByTag("font").get(0).text();
                    bidModel.setTitle(title);
                    //时间
                    String time = tdTitle.getElementsByTag("font").get(1).text();
                    bidModel.setTime(time);

                    Element tdContent = document.getElementById("TDContent");
                    bidModel.setContent(tdContent.html());
                    list.add(bidModel);
                    title = null;
                    time = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        return list;
    }

}
