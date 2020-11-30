package com.wh.yaofangwang.service;

import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.common.DataParse;
import com.wh.yaofangwang.common.ExcelUtil;
import com.wh.yaofangwang.common.LogUtil;
import com.wh.yaofangwang.thread.DataCrawlerThread;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.FutureTask;

/**
 * @ClassName DataCrawler
 * @Description
 * @Author wh
 * @Date 2020/6/3 9:50
 */
@Service
public class DataCrawlerServiceImpl {

    public void writeFile(){
        Map<String, List<LinkedHashMap<String, Object>>> listMap = DataParse.crawlerYaofangwang();
        LogUtil.info("数据爬取完毕, 创建excel工作簿");
        HSSFWorkbook wb = new HSSFWorkbook();
        LogUtil.info("开始写excel");
        for (Map.Entry entry: listMap.entrySet()) {
            LogUtil.info("写入：" + entry.getKey() + "sheet开始");
            ExcelUtil.createHSSFWorkbook(wb, (String) entry.getKey(),"E:\\",
                    "药房网", (List<LinkedHashMap>) entry.getValue());
            LogUtil.info("写入：" + entry.getKey() + "sheet完成");
        }
        LogUtil.info("写入excel完成");
    }

    public void writeFileThread(){

        HSSFWorkbook wb = new HSSFWorkbook();
        List<String> list = new ArrayList<>();
        Map<String, String> areaMap = DataParse.getAreaMap();
        int count = 0;
        for (Map.Entry entry :areaMap.entrySet()) {
//            DataCrawlerThread callThread = new DataCrawlerThread(wb,
//                    (String) entry.getKey(), (String) entry.getValue());
            count ++;
            FutureTask<String> ft = new FutureTask<>(new DataCrawlerThread(wb,
                    (String) entry.getKey(), (String) entry.getValue()));
            Thread thread = new Thread(ft);
            thread.setName((String) entry.getKey() + count);
            thread.start();
//            LogUtil.info(thread.getName());
            /*try {
                list.add(ft.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }*/

        }

        /*LogUtil.info("++++++++++++++++++++++++结束+++++++++++++++++++++++++++");
        LogUtil.info(list.toString());
        LogUtil.info("" + list.size());*/
    }

    public String crawlerYaofangwangResp(){
        String url = "https://www.yaofangwang.com/yiyuan/hostpitallist";
        try {
            Connection.Response execute = Jsoup.connect(url)
                    .header("method", "POST")
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "zh-CN,zh;q=0.9")
                    .header("Connection", "keep-alive")
                    .header("Content-Length", "65")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Host", "www.yaofangwang.com")
                    .header("Origin", "https://www.yaofangwang.com")
//                    .header("Referer", "https://www.yaofangwang.com/yiyuan/r2610/")
                    .header("User-Agent", "PostmanRuntime/7.25.0")
//                    .header("", "")
//                    .header("", "")
                    .data("region_id", "2610")
                    .data("para", "all-all-all-all-all")
                    .data("page_index", "1")
//                    .data("page_size", "20")
                    .timeout(0)
                    .execute();

            LogUtil.info("execute:" + execute.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        /*String url = "https://www.yaofangwang.com/yiyuan/361292.html";
        Document document = DataCamling.docCamling(url);
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

        List<LinkedHashMap> list = new ArrayList<>();
        list.add(map);

        List<String> sss = new ArrayList<>();
        sss.add("ddd");
        sss.add("ffff");
        sss.add("gfdsg");
        HSSFWorkbook wb = new HSSFWorkbook();
        for (int i = 0; i < sss.size(); i++) {
            ExcelUtil.createHSSFWorkbook(wb, sss.get(i), "E:\\",
                    "test.xlsx", list);
        }*/

        DataCamling.writeFile("测试数据", "E:\\药房网数据抓取丢失数据.txt");
    }
}
