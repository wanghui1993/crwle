package com.wh.yaofangwang.common;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wh.yaofangwang.model.YaofwPO;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DataParse
 * @Description
 * @Author wh
 * @Date 2020/6/4 9:30
 */
public class DataParse {

    public static Map<String, String> getAreaMap(){
        Map<String, String> map = new HashMap<>();
        try {
            Document document = DataCamling.docCamling("https://www.yaofangwang.com/yiyuan/r0/");
            Element body = document.body();
            Elements elementsByClass = body.getElementById("wrap").getElementsByClass("select clearfix");
            /*Elements opt = elementsByClass.get(0)
                    .getElementsByClass("opt").get(0)
                    .getElementsByAttribute("b");
            LogUtil.info("opt:" + opt);*/
            Element sitems_tl = elementsByClass.get(0)
                    .getElementsByClass("sitems tl").get(0);
            Element li = sitems_tl.getElementsByTag("li").get(0);
            Elements a = li.getElementsByTag("a");
            for (Element e_href :a) {
                String href = e_href.attr("href");
//                LogUtil.info("href: " + href);
//                String substring = href.substring(href.replaceFirst("/", "").indexOf("/") + 2, href.lastIndexOf("/"));
                if (!href.endsWith("/")) continue;
                String substring = href.substring(href.replaceFirst("/", "?").indexOf("/"), href.lastIndexOf("/"));
                map.put(e_href.text(), substring.substring(substring.indexOf("r") + 1));
            }
            LogUtil.info("map: " + map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, List<String>> crawlerYaofangwangPath(Map<String, String> areaMap){
        String url = "https://www.yaofangwang.com/yiyuan/hostpitallist?para=all-all-all-all-all&region_id=ri_wh&page_index=pi_wh";
        Map<String, List<String>> resultMap = new HashMap<>();
        try {
//            Map<String, String> areaMap = this.getAreaMap();
            for (Map.Entry entry :areaMap.entrySet()) {
                String tmpUrl = url;
                tmpUrl = tmpUrl.replace("ri_wh", entry.getValue().toString());
                //获取第一页数据
                String tmpUrl_f = tmpUrl.replace("pi_wh", "1");
//                LogUtil.info(tmpUrl_f);
                Connection.Response response = DataCamling.docCamlingExecute(tmpUrl_f);
                JSONObject jsonObject = JSONUtil.parseObj(response.body());
                Map<String, Object> map = parseYaofangwang(jsonObject);
                List<YaofwPO> yaofwPOS = (List<YaofwPO>) map.get("list");
                //获取第二页及以后数据
                for (int i = 2; i <= (Integer) map.get("pageCount"); i++) {
                    String tmpUrl_s = tmpUrl.replace("pi_wh", String.valueOf(i));
//                    LogUtil.info(tmpUrl_s);
                    Connection.Response response_s = DataCamling.docCamlingExecute(tmpUrl_s);
                    JSONObject jsonObject_s = JSONUtil.parseObj(response_s.body());
                    yaofwPOS.addAll((Collection<? extends YaofwPO>) parseYaofangwang(jsonObject_s).get("list"));
                }
                List<String> ids = yaofwPOS.stream().map(YaofwPO::getId).collect(Collectors.toList());

                resultMap.put(entry.getKey().toString(), ids);
                LogUtil.info("收集列表数据" + resultMap);
                tmpUrl = null;
                yaofwPOS = null;
                map = null;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static Map<String, Object> parseYaofangwang(JSONObject jsonObject){
        Map<String, Object> resultMap = new HashMap<>();
        String result = jsonObject.getStr("result");
        JSONObject jsonResult = JSONUtil.parseObj(result);
        JSONArray items = jsonResult.getJSONArray("items");
        Integer pageCount = jsonResult.getInt("page_count");
        resultMap.put("pageCount", pageCount);
        List<YaofwPO> yaofwPOS = JSONUtil.toList(items, YaofwPO.class);
        resultMap.put("list", yaofwPOS);
        return resultMap;
    }

    public static LinkedHashMap<String, Object> parseYaofangwangDefault(Document document){
        Element wrap = document.getElementById("wrap");
        Element yy_clearfix = wrap.getElementsByClass("yy clearfix").get(0);
        Element left = yy_clearfix.getElementsByClass("left").get(0);
        Element ymain_clearfix = left.getElementsByClass("ymain clearfix").get(0);
        Element info = ymain_clearfix.getElementsByClass("info").get(0);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        String h1 = info.getElementsByTag("h1").get(0).text();
        map.put("医院名称", h1);

        Elements p_list = info.getElementsByTag("p");
        for (int i = 0; i < p_list.size(); i++) {
            Element p = p_list.get(i);
            String p_str = p.text();
            String[] p_arr = p_str.split("：");
            if (i == p_list.size()-1){
                String[] ss = p_arr[1].split("　　");
                map.put(p_arr[0], ss[0]);
                map.put(ss[1], p_arr[p_arr.length-1]);
            } else {
                map.put(p_arr[0], p_arr[p_arr.length-1]);
            }

        }
        return map;
    }

    public static Map<String, List<LinkedHashMap<String, Object>>> crawlerYaofangwang() {

        Map<String, List<LinkedHashMap<String, Object>>> resultMap = new HashMap<>();
        String defaultUrl = "https://www.yaofangwang.com/yiyuan/id_wh.html";
        //获取地区
        Map<String, String> areaMap = getAreaMap();
        //获取详情页id与总页数
        Map<String, List<String>> listMap = crawlerYaofangwangPath(areaMap);
        //获取详细信息
        for (Map.Entry entry :listMap.entrySet()) {
            List<String> ids = (List<String>) entry.getValue();
            List<LinkedHashMap<String, Object>> result = new ArrayList<>();
            //访问详情页
            ids.stream().forEach(id -> {
                String tmpUrl = defaultUrl.replace("id_wh", id);
                LogUtil.info(entry.getKey() + "tmpUrl:" + tmpUrl);
                try {
                    Document document = DataCamling.docCamling(tmpUrl);
                    //解析详情页
                    LinkedHashMap<String, Object> map = parseYaofangwangDefault(document);
                    result.add(map);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            resultMap.put((String) entry.getKey(), result);
        }
        LogUtil.info("数据爬取完毕");
        return resultMap;
    }

}
