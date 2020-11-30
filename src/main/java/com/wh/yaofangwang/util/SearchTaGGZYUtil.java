package com.wh.yaofangwang.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
 * @ClassName SearchTaGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/20
 */
public class SearchTaGGZYUtil {

    static String GGZY_TA_ROOT_URL = "http://www.taggzyjy.com.cn:8081/";
    static String GGZY_TA_URL = "http://www.taggzyjy.com.cn:8081/inteligentsearch/rest/inteligentSearch/getFullTextData";

    static String data = "{\"token\":\"\",\"pn\":pn_param,\"rn\":10,\"sdt\":\"\",\"edt\":\"\",\"wd\":\"search_param\"," +
            "\"inc_wd\":\"\",\"exc_wd\":\"\",\"fields\":\"title;content\",\"cnum\":\"001\",\"sort\":\"{\\\"webdate\\\":0}\"," +
            "\"ssort\":\"title\",\"cl\":500,\"terminal\":\"\",\"condition\":null,\"time\":null,\"highlights\":\"title;content\"," +
            "\"statistics\":null,\"unionCondition\":null,\"accuracy\":\"\",\"noParticiple\":\"1\",\"searchRange\":null}";

    public static LinkedHashMap<String, List<String>> searchTAGGZYLoop(int bidType, Map<String, List<String>> resultMap){

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();

        Arrays.stream(Constants.params).forEach(str -> {
            List<String> hrefs = new ArrayList<>();
            List<String> oldHrefs = null;
            if (resultMap != null)
                oldHrefs = resultMap.get(str);
            try {
                boolean flag = true;
                int count = 0;
                while (flag){
                    //获取超链接
                    String tempData = data.replace("pn_param", String.valueOf(count));
                    tempData = tempData.replace("search_param", str);
                    System.out.println("--------------------------:" + tempData);
                    String post = DataCamling.postWithRequest(GGZY_TA_URL, tempData);
                    JSONObject postJobj = JSONUtil.parseObj(post);
                    JSONObject result = postJobj.getJSONObject("result");
                    JSONArray records = result.getJSONArray("records");
                    if (CollectionUtils.isEmpty(records)) flag = false;
                    for (Object obj :records) {
                        JSONObject jObj = (JSONObject) obj;
                        String linkurl = jObj.getStr("linkurl");
                        linkurl = GGZY_TA_ROOT_URL + linkurl;
                        if (hrefs.contains(linkurl)) flag = false;
                        String title = jObj.getStr("title");
                        if (oldHrefs != null && oldHrefs.contains(linkurl)) {flag = false; break;}
                        if (title.contains("中标") || title.contains("招标")
                                || title.contains("成交") || title.contains("合同"))
                            hrefs.add(linkurl);
                        linkurl = null;
                        title = null;
                        jObj = null;
                    }
                    count += 10;
                    result = null;
                    postJobj = null;
                    records = null;
                    post = null;
                    tempData = null;
                }
                map.put(str, hrefs);
                hrefs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    public static List<BidModel> searchTAGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Elements ewbArticles = document.getElementsByClass("article-info");
                    if (!CollectionUtils.isEmpty(ewbArticles)) {
                        Element element = ewbArticles.get(0);
                        //标题
                        String title = element.getElementsByTag("h1").get(0).text();
                        bidModel.setTitle(title);
                        //时间
                        String time = element.getElementsByClass("infotime").get(0).text();
                        bidModel.setTime(time.substring(1, time.length()-2));

                        bidModel.setContent(element.html());
                        list.add(bidModel);
                        title = null;
                        time = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        return list;
    }

}
