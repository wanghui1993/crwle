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
public class SearchZbGGZYUtil {

    static String GGZY_BZ_ROOT_URL = "http://ggzyjy.zibo.gov.cn/";
    static String GGZY_BZ_URL = "http://ggzyjy.zibo.gov.cn/TPFront/showinfo/searchresult.aspx?" +
            "EpStr3=search_param&searchtype=title";

    static String VIEWSTATE =
            "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";

    public static LinkedHashMap<String, List<String>> searchZBGGZYLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("__VIEWSTATE", VIEWSTATE);
        params.put("__VIEWSTATEGENERATOR", "1EAF79DA");
        params.put("__EVENTTARGET", "SearchResult1$Pager");
        params.put("__VIEWSTATEENCRYPTED", "");
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZY_BZ_URL.replace("search_param", str);
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

    public static List<BidModel> searchZBGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    //标题
                    String title = document.getElementById("Title").text();
                    bidModel.setTitle(title);
                    //时间
                    Elements times = document.getElementsByClass("webfont");
                    if (!CollectionUtils.isEmpty(times)) {
                        String time = times.get(0).text();
                        bidModel.setTime(time.substring(6, 16));
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
