package com.wh.yaofangwang.util;

import com.wh.yaofangwang.common.Constants;
import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.model.bid.BidModel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SearchDzGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/16
 */
public class SearchDzGGZYUtil {
    static String GGZYJYZX_DZ_ROOT_URL = "http://ggzyjy.dezhou.gov.cn/";
    static String GGZYJYZX_DZ_URL = "http://ggzyjy.dezhou.gov.cn/TPFront/showinfo/SearchResult.aspx?" +
            "EpStr3=search_param&Paging=pageIndex_param";

    public static LinkedHashMap<String, List<String>> searchDzGGZYLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZYJYZX_DZ_URL.replace("search_param", str);
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
                    Element tdcontent = doc.getElementById("tdcontent");
                    Elements aList = tdcontent.select("a");
                    if (aList == null || aList.size() <= 0) flag = false;
                    for (Element a : aList) {
                        String href = a.attr("href");
                        href = GGZYJYZX_DZ_ROOT_URL + href;
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

    public static List<BidModel> searchDzGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Elements elements = document.getElementsByClass("article-block");
                    if (!CollectionUtils.isEmpty(elements)) {
                        Element element = elements.get(0);
                        String title = null;
                        if (element.getElementsByTag("h2").size() > 0)
                            title = element.getElementsByTag("h2").get(0).text();
                        /*else if (element.getElementsByClass("tle").size() > 0)
                            title = element.getElementsByClass("tle").get(0).text();*/
                        bidModel.setTitle(title);
                        Elements infoSources = element.getElementsByClass("info-sources");
                        if (!CollectionUtils.isEmpty(infoSources)){
                            String time = infoSources.get(0).text();
                            bidModel.setTime(time);
                        }
                        Elements contents = element.getElementsByClass("article-content");
                        if (!CollectionUtils.isEmpty(contents)) {
                            String html = contents.get(0).html();
                            bidModel.setContent(html);
                            html = null;
                        }
                        if (StringUtils.isEmpty(title) || title.contains("招标") || title.contains("中标"))
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
