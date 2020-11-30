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
 * @ClassName SearchJnGgzyUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/15
 */
public class SearchJnGgzyUtil {

    static String GGZYJYZX_ROOT_URL = "http://jnggzy.jinan.gov.cn/";
    static String GGZYJYZX_URL = "http://jnggzy.jinan.gov.cn/jrobot/search.do?" +
            "webid=103&pg=10&p=pageIndex_param&tpl=103&category=&colid=&analyzeType=1&" +
            "criteria_TYPE=&pos=&od=1&date=startDate_param&date=endDate_param&" +
            "q=search_param&criteria_tag=criteriaTag_param";

    public static LinkedHashMap<String, List<String>> searchGgzyjyzxLoop(int bidType, Map<String, List<String>> resultMap){
        //参数拼接 时间
        Map<String, String> dateMap = DateUtil.calaDate(bidType, "");
        String startTime = dateMap.get("startTime");
        String endTime = dateMap.get("endTime");
        GGZYJYZX_URL = GGZYJYZX_URL.replace("startDate_param", startTime);
        GGZYJYZX_URL = GGZYJYZX_URL.replace("endDate_param", endTime);

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZYJYZX_URL.replace("search_param", str);
            //查询类型
            if (bidType == 1) tempUrl = tempUrl.replace("criteriaTag_param", "招标公告");
            if (bidType == 7) tempUrl = tempUrl.replace("criteriaTag_param", "中标公告");
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
                    if (doc.getElementsByClass("jsearch-result-box").size() <= 0) flag = false;
                    Elements resultTile = doc.getElementsByClass("jsearch-result-box");
                    if (resultTile.size() <= 0) flag = false;
                    for (Element title : resultTile) {
                        Element a = title.select("a").first();
                        String href = a.attr("href");
                        href = GGZYJYZX_ROOT_URL + href;
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

    public static List<BidModel> searchGgzyjyzxDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Elements elements = document.getElementsByClass("list");
                    if (!CollectionUtils.isEmpty(elements)) {
                        Element element = elements.get(0);
                        String title = null;
                        if (element.getElementsByTag("h1").size() > 0)
                            title = element.getElementsByTag("h1").get(0).text();
                        else if (element.getElementsByClass("tle").size() > 0)
                            title = element.getElementsByClass("tle").get(0).text();
                        bidModel.setTitle(title);
                        String html = element.html();
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
