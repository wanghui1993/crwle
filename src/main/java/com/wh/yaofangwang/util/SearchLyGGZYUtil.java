package com.wh.yaofangwang.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wh.yaofangwang.common.Constants;
import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.model.bid.BidModel;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SearchLyGGZYUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/20
 */
public class SearchLyGGZYUtil {

    static String GGZY_LY_ROOT_URL = "http://ggzyjy.linyi.gov.cn/linyi/";
    static String GGZY_LY_URL = "http://ggzyjy.linyi.gov.cn/inteligentsearch/rest/esinteligentsearch/getFullTextDataNew";

    static String data = "{\"token\":\"\",\"pn\":pn_param,\"rn\":10,\"sdt\":\"\",\"edt\":\"\"," +
            "\"wd\":\"search_param\",\"inc_wd\":\"\",\"exc_wd\":\"\",\"fields\":\"title;content\",\"cnum\":\"001\"," +
            "\"sort\":\"\",\"ssort\":\"title\",\"cl\":500,\"terminal\":\"\",\"condition\":null,\"time\":null," +
            "\"highlights\":\"title;content\",\"statistics\":null,\"unionCondition\":null,\"accuracy\":\"\"," +
            "\"noParticiple\":\"0\",\"searchRange\":null}";

    public static LinkedHashMap<String, List<String>> searchLYGGZYLoop(int bidType, Map<String, List<String>> resultMap){

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
                    String post = DataCamling.postWithRequest(GGZY_LY_URL, tempData);
                    JSONObject postJobj = JSONUtil.parseObj(post);
                    JSONObject result = postJobj.getJSONObject("result");
                    JSONArray records = result.getJSONArray("records");
                    if (CollectionUtils.isEmpty(records)) flag = false;
                    for (Object obj :records) {
                        JSONObject jObj = (JSONObject) obj;
                        String linkurl = jObj.getStr("linkurl");
                        linkurl = GGZY_LY_ROOT_URL + linkurl;
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

    public static List<BidModel> searchLYGGZYDetail(LinkedHashMap<String, List<String>> maps){
        List<BidModel> list = new ArrayList<>();
        maps.entrySet().stream().forEach(map -> {
            map.getValue().stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(map.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    Elements ewbArticles = document.getElementsByClass("ewb-article");
                    if (!CollectionUtils.isEmpty(ewbArticles)) {
                        Element element = ewbArticles.get(0);
                        //标题
                        String title = element.getElementsByTag("h3").get(0).text();
                        bidModel.setTitle(title);
                        //时间
                        String time = element.getElementById("zhuanzai").text();
                        bidModel.setTime(time.substring(6, 16));

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
