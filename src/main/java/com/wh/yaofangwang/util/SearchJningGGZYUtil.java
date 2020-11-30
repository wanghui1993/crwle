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
/*public class SearchJningGGZYUtil {

    static String GGZY_JNING_ROOT_URL = "http://www.zzggzy.com/";
    static String GGZY_JNING_URL = "http://ggzy.jining.gov.cn/JiNing/Bulletins?CategoryCode=CategoryCode_param";

    public static Map<String, List<String>> searchZZGGZYLoop(int bidType){

        String tempUrl = null;
        if (bidType == 1) tempUrl = GGZY_JNING_URL.replace("CategoryCode_param", "");
        System.out.println("+++++++++++++++++++++:" + tempUrl);
        Map<String, List<String>> map = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("__VIEWSTATEGENERATOR", "1EAF79DA");
        params.put("__EVENTTARGET", "SearchResult1$Pager");
        params.put("__VIEWSTATEENCRYPTED", "");
        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
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

    public static List<BidModel> searchZZGGZYDetail(Map<String, List<String>> maps){
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
                    bidModel.setTitle(time);

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

}*/
