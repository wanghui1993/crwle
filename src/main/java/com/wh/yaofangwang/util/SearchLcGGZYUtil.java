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

import static com.wh.yaofangwang.common.DataCamling.docCamling;

/**
 * @ClassName SearchDzGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/16
 */
public class SearchLcGGZYUtil {
    static String GGZYJYZX_LC_ROOT_URL = "http://www.lcsggzyjy.cn/";
    static String GGZYJYZX_LC_URL = "http://www.lcsggzyjy.cn/lcweb/showinfo/SearchResult.aspx?" +
            "keyword=search_param&Paging=pageIndex_param";

    public static LinkedHashMap<String, List<String>> searchLCGGZYLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            //替换查询条件
            String tempUrl = GGZYJYZX_LC_URL.replace("search_param", str);
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
                    Document doc = docCamling(pageUrl);
                    Element tdcontent = doc.getElementById("tdcontent");
                    Elements aList = tdcontent.select("a");
                    if (aList == null || aList.size() <= 0) flag = false;
                    for (Element a : aList) {
                        String href = a.attr("href");
                        href = GGZYJYZX_LC_ROOT_URL + href;
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

    public static List<BidModel> searchLCGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            for (String href : map.getValue()) {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = docCamling(href);
                    //一种页面格式
                    Elements elements = document.getElementsByClass("detail-content");
                    if (!CollectionUtils.isEmpty(elements)) {
                        Element element = elements.get(0);
                        String title = null;
                        if (element.getElementsByTag("h3").size() > 0)
                            title = element.getElementsByTag("h3").get(0).text();
                        bidModel.setTitle(title);
                        Elements subCp = element.getElementsByClass("sub-cp");
                        if (!CollectionUtils.isEmpty(subCp)) {
                            String time = subCp.get(0).text();
                            time = time.substring(6, 15);
                            bidModel.setTime(time);
                        }
                        Element mainContent = document.getElementById("mainContent");
                        if (mainContent != null) {
                            String html = mainContent.html();
                            bidModel.setContent(html);
                            html = null;
                        }
                        bidModel.setBidType(3);
                        title = null;
                    } else {
                        //第二种页面类型
                        Element menutab6_1 = document.getElementById("menutab_6_1");
                        Element menutab6_3 = document.getElementById("menutab_6_3");
                        Element menutab6_5 = document.getElementById("menutab_6_5");
                        BidModel getdetail = getdetail(menutab6_3);
                        if (getdetail != null) continue;
                        BidModel getdetail_6 = getdetail(menutab6_5);
                        getdetail_6.setBidType(7);
                        if (getdetail_6 == null){
                            getdetail_6 = getdetail(menutab6_1);
                            getdetail_6.setBidType(1);
                        }
                        bidModel.setTitle(getdetail_6.getTitle());
                        bidModel.setTime(getdetail_6.getTime());
                        bidModel.setContent(getdetail_6.getContent());
                    }
                    list.add(bidModel);
                    bidModel = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return list;
    }

    private static BidModel getdetail(Element menutab6_x) {
        BidModel bidModel = new BidModel();
        if (menutab6_x != null){
            Element secondPageType = menutab6_x.getElementById("ZtbzbggDetail_jsgc1_content");
            if (secondPageType != null){
                Elements msoNormals = secondPageType.getElementsByClass("MsoNormal");
                if (!CollectionUtils.isEmpty(msoNormals)){
                    String title = msoNormals.get(0).text();
                    bidModel.setTitle(title);
                    String time = msoNormals.get(msoNormals.size()-1).text();
                    bidModel.setTime(time);
                    bidModel.setContent(secondPageType.html());
                }
            }
        }
        return bidModel;
    }

}
