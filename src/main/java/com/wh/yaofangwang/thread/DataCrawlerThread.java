package com.wh.yaofangwang.thread;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.common.ExcelUtil;
import com.wh.yaofangwang.common.LogUtil;
import com.wh.yaofangwang.model.YaofwPO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @ClassName DataCrawlerThread
 * @Description
 * @Author wh
 * @Date 2020/6/4 9:36
 */
public class DataCrawlerThread implements Callable<String> {

    private HSSFWorkbook wb;
    private String provinceName;
    private String provinceCode;

    public DataCrawlerThread(HSSFWorkbook wb, String provinceName, String provinceCode) {
        this.wb = wb;
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
    }

    private Map<String, List<String>> crawlerYaofangwangPath(){
        String url = "https://www.yaofangwang.com/yiyuan/hostpitallist?para=all-all-all-all-all&region_id=ri_wh&page_index=pi_wh";
        Map<String, List<String>> resultMap = new HashMap<>();

        String errorUrl = "";
        try {
            String tmpUrl = url;
            tmpUrl = tmpUrl.replace("ri_wh", this.provinceCode);
            //获取第一页数据
            String tmpUrl_f = tmpUrl.replace("pi_wh", "1");
    //                LogUtil.info(tmpUrl_f);
            errorUrl = tmpUrl_f;
            Connection.Response response = DataCamling.docCamlingExecute(tmpUrl_f);
            JSONObject jsonObject = JSONUtil.parseObj(response.body());
            Map<String, Object> map = this.parseYaofangwang(jsonObject);
            List<YaofwPO> yaofwPOS = (List<YaofwPO>) map.get("list");
            //获取第二页及以后数据
            for (int i = 2; i <= (Integer) map.get("pageCount"); i++) {
                String tmpUrl_s = tmpUrl.replace("pi_wh", String.valueOf(i));
    //                    LogUtil.info(tmpUrl_s);
                errorUrl = tmpUrl_s;
                Connection.Response response_s = DataCamling.docCamlingExecute(tmpUrl_s);
                JSONObject jsonObject_s = JSONUtil.parseObj(response_s.body());
                yaofwPOS.addAll((Collection<? extends YaofwPO>) this.parseYaofangwang(jsonObject_s).get("list"));
                tmpUrl_s = null;
            }
            List<String> ids = yaofwPOS.stream().map(YaofwPO::getId).collect(Collectors.toList());

            resultMap.put(this.provinceName, ids);
            LogUtil.info(this.provinceName + "收集列表数据" + resultMap);
            tmpUrl = null;
            tmpUrl_f = null;
            yaofwPOS = null;
            map = null;

        } catch (IOException e) {
//            e.printStackTrace();
            DataCamling.writeFile(this.provinceName + " ： " + errorUrl, "E:\\药房网数据列表抓取丢失数据.txt");
        }
        return resultMap;
    }

    private Map<String, Object> parseYaofangwang(JSONObject jsonObject){
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

    private LinkedHashMap<String, Object> parseYaofangwangDefault(Document document){
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

    public Map<String, List<LinkedHashMap<String, Object>>> crawlerYaofangwang() {

        Map<String, List<LinkedHashMap<String, Object>>> resultMap = new HashMap<>();
        String defaultUrl = "https://www.yaofangwang.com/yiyuan/id_wh.html";
        //获取详情页id与总页数
        Map<String, List<String>> listMap = this.crawlerYaofangwangPath();
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
                    LinkedHashMap<String, Object> map = this.parseYaofangwangDefault(document);
                    result.add(map);
                } catch (IOException e) {
//                    e.printStackTrace();
                    DataCamling.writeFile(this.provinceName + " : " + tmpUrl, "E:\\药房网详情数据抓取丢失数据.txt");
                }
            });
            resultMap.put((String) entry.getKey(), result);
        }
        LogUtil.info(this.provinceName + "数据爬取完毕");
        return resultMap;
    }

    public String writeFile(){
        Map<String, List<LinkedHashMap<String, Object>>> listMap = this.crawlerYaofangwang();
        LogUtil.info(this.provinceName + "数据爬取完毕, 创建excel工作簿");
//        HSSFWorkbook wb = new HSSFWorkbook();
        LogUtil.info(this.provinceName + "开始写excel");
        for (Map.Entry entry: listMap.entrySet()) {
            LogUtil.info("写入：" + entry.getKey() + "sheet开始");
            ExcelUtil.createHSSFWorkbook(wb, (String) entry.getKey(),"E:\\",
                    "药房网数据抓取.xls", (List<LinkedHashMap>) entry.getValue());
            LogUtil.info("写入：" + entry.getKey() + "sheet完成");
        }
        LogUtil.info(this.provinceName + "写入excel完成");
        return this.provinceName;
    }

    /*@Override
    public void run() {
        this.writeFile();
    }*/

    @Override
    public String call() throws Exception {
        return this.writeFile();
    }
}
