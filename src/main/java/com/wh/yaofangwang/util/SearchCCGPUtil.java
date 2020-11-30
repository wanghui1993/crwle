package com.wh.yaofangwang.util;

import com.wh.yaofangwang.common.Constants;
import com.wh.yaofangwang.common.DataCamling;
import com.wh.yaofangwang.common.LogUtil;
import com.wh.yaofangwang.model.bid.BidModel;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName SearchCCGPUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/13
 */
public class SearchCCGPUtil {

    /*static String CCGP_URL = "http://search.ccgp.gov.cn/bxsearch?" +
            "searchtype=2&page_index=1&pinMu=0&bidType=bidType_param&dbselect=bidx&kw=ccgp_param&" +
            "start_time=2020:10:06&end_time=2020:10:13&timeType=2&displayZone=山东省&" +
            "zoneId=37 not 3702";*/

    static String CCGP_URL = "http://search.ccgp.gov.cn/bxsearch?" +
            "searchtype=2&page_index=pageIndex_param&bidSort=&buyerName" +
            "=&projectId=&pinMu=0&bidType=bidType_param&dbselect=bidx&kw=ccgp_param" +
            "&start_time=startTime_param&end_time=endTime_param&timeType=timeType_param&displayZone=山东省" +
            "&zoneId=37 not 3702&pppStatus=0&agentName=";

    //公开招标数据
    public static List<BidModel> beginSearch(String bidType) throws IOException {

        List<BidModel> list = null;
        LinkedHashMap<String, List<String>> listMap = searchListLoop(bidType, null);
        if ("1".equals(bidType)) { //招标
            list = searchOpenTenderDetail(listMap);//生成excel
        } else if ("7".equals(bidType)) { //中标
            list = searchWinningBidDetail(listMap);
        }
        return list;
    }

    //列表循环查询
    public static List<BidModel> searchListLoopWeb(String bidType){
        String TEMP_CCGP_URL = CCGP_URL.replace("bidType_param", bidType);
        String[] params = Constants.params;

        List<BidModel> hrefs = new ArrayList<>();
        Arrays.stream(params).forEach(str -> {
            List<String> tempList = new ArrayList<>();
            String tempUrl = TEMP_CCGP_URL.replace("ccgp_param", str);
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            int month = ca.get(Calendar.MONTH) + 1;
            if ("1".equals(bidType)) {
                //中标最近一个月
                tempUrl = tempUrl.replace("timeType_param", "4");
                tempUrl = tempUrl.replace("endTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                ca.add(Calendar.MONTH, -3);
                month = ca.get(Calendar.MONTH) + 1;
                tempUrl = tempUrl.replace("startTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                LogUtil.info("招标CCGP_URL：" + tempUrl);
            }
            else if ("7".equals(bidType)) {
                //中标两年内
                tempUrl = tempUrl.replace("timeType_param", "6");
                tempUrl = tempUrl.replace("endTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                ca.add(Calendar.YEAR, -1);
                tempUrl = tempUrl.replace("startTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                LogUtil.info("中标CCGP_URL：" + tempUrl);
            }
            try {
                boolean flag = true;
                int count = 1;
                while (flag){
                    //获取超链接
                    String pageUrl = tempUrl.replace("pageIndex_param", String.valueOf(count));
                    System.out.println("pageUrl:"+pageUrl);
                    Document doc = DataCamling.docCamling(pageUrl);
                    if (doc.getElementsByClass("vT-srch-result-list-bid").size() <= 0) flag = false;
                    Element element = doc.getElementsByClass("vT-srch-result-list-bid").get(0);
                    Elements lis = element.getElementsByTag("li");
                    if (lis.size() <= 0) flag = false;
                    for (Element li : lis) {
                        BidModel bidModel = new BidModel();
                        Element a = li.select("a").first();
                        String href = a.attr("href");
                        Element span = li.getElementsByTag("span").get(0);
                        String time = span.text().substring(0, 20);
                        String title = a.text();
                        bidModel.setTitle(title);
                        bidModel.setHref(href);
                        bidModel.setTime(time);
                        bidModel.setKeyWord(str);
                        if (tempList.contains(href)) flag = false;
                        tempList.add(href);
                        hrefs.add(bidModel);
                        bidModel = null;
                    }
                    count ++;
                    pageUrl = null;
                }
                tempUrl = null;
                tempList = null;
            } catch (IOException e) {
                LogUtil.info("CCGP查询列表异常");
                e.printStackTrace();
            }
        });
        return hrefs;
    }

    //列表循环查询
    public static LinkedHashMap<String, List<String>> searchListLoop(String bidType, Map<String, List<String>> resultMap) throws IOException {
        String TEMP_CCGP_URL = CCGP_URL.replace("bidType_param", bidType);
        String[] params = Constants.params;
        LinkedHashMap<String, List<String>> map = new LinkedHashMap<>();
        Arrays.stream(params).forEach(str -> {
            List<String> oldHrefs = null;
            if (resultMap != null)
                oldHrefs = resultMap.get(str);
            List<String> hrefs = new ArrayList<>();
            String tempUrl = TEMP_CCGP_URL.replace("ccgp_param", str);
            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            int month = ca.get(Calendar.MONTH) + 1;
            if ("1".equals(bidType)) {
                //中标最近一个月
                tempUrl = tempUrl.replace("timeType_param", "4");
                tempUrl = tempUrl.replace("endTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                ca.add(Calendar.MONTH, -3);
                month = ca.get(Calendar.MONTH) + 1;
                tempUrl = tempUrl.replace("startTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                LogUtil.info("招标CCGP_URL：" + tempUrl);
            } else if ("7".equals(bidType)) {
                //中标两年内
                tempUrl = tempUrl.replace("timeType_param", "6");
                tempUrl = tempUrl.replace("endTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                ca.add(Calendar.YEAR, -1);
                tempUrl = tempUrl.replace("startTime_param",
                        ca.get(Calendar.YEAR) + ":" + month + ":" + ca.get(Calendar.DAY_OF_MONTH));
                LogUtil.info("中标CCGP_URL：" + tempUrl);
            }
            try {
                boolean flag = true;
                int count = 1;
                while (flag){
                    //获取超链接
                    String pageUrl = tempUrl.replace("pageIndex_param", String.valueOf(count));
                    System.out.println("pageUrl:"+pageUrl);
                    Document doc = DataCamling.docCamling(pageUrl);
                    if (doc.getElementsByClass("vT-srch-result-list-bid").size() <= 0) flag = false;
                    Element element = doc.getElementsByClass("vT-srch-result-list-bid").get(0);
                    Elements lis = element.getElementsByTag("li");
                    if (lis.size() <= 0) flag = false;
                    for (Element li : lis) {
                        Element a = li.select("a").first();
                        String href = a.attr("href");
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
            } catch (IOException e) {
                LogUtil.info("CCGP查询列表异常");
                e.printStackTrace();
            }
        });
        return map;
    }

    //获取招标详情页息
    public static List<BidModel> searchOpenTenderDetail(LinkedHashMap<String, List<String>> paramMap) {

        List<BidModel> list = new ArrayList<>();
        for (Map.Entry entry : paramMap.entrySet()) {
            List<String> hrefs = (List<String>) entry.getValue();
            hrefs.stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    bidModel.setKeyWord(entry.getKey().toString());
                    Document document = DataCamling.docCamling(href);
                    //获取招标标题
                    String title = document.getElementsByTag("h2").get(0).text();
                    bidModel.setTitle(title);
                    //获取招标详情
                    Element noticeDetail = document.getElementById("NoticeDetail");
                    if (noticeDetail != null
                            && noticeDetail.getElementsByTag("tbody").size() > 0) {
                        bidModel.setContent(noticeDetail.html());
                        /*Element tbody = noticeDetail.getElementsByTag("tbody").get(0);
                        Elements trs = tbody.getElementsByTag("tr");
                        for (int i = 1; i < trs.size(); i++) {
                            String name = trs.get(i).getElementsByTag("td").get(1).text();
//                            map.put("name", name);
                            bidModel.setName(name);
                            String supplierName = trs.get(i).getElementsByTag("td").get(2).text();
//                            map.put("supplierName", supplierName);
                            bidModel.setSupplierName(supplierName);
                            String costs = trs.get(i).getElementsByTag("td").get(4).text();
                            bidModel.setCosts(costs);
//                            map.put("param", entry.getKey().toString());
                            bidModel.setKeyWord(entry.getKey().toString());
                        }*/

                        Element eDetail = document.getElementsByClass("vF_detail_content").get(0);
                        //获取招标时间
                        String detailContent = eDetail.text();
                        if (detailContent.lastIndexOf("开标时间及地点") > 0){
                            detailContent = detailContent.substring(detailContent.lastIndexOf("开标时间及地点"));
                            String[] s = detailContent.split(" ");
//                            map.put("openTendertime", s[1]);
                            bidModel.setTime(s[1]);
//                            maps.add(map);
                        }
                        /*else {
//                            LogUtil.debug("-------:" + href);
                        }*/
                    }
                    /*else {
                        LogUtil.debug("++++++:" + href);
                    }*/
                    list.add(bidModel);
                    bidModel = null;
                } catch (IOException e) {
                    LogUtil.error("CCGP查询详情失败");
                    e.printStackTrace();
                }
            });
        }
        return list;
    }

    //中标公告数据
    public static List<BidModel> searchWinningBidDetail(Map<String, List<String>> paramMap) {

        List<BidModel> list = new ArrayList<>();
        for (Map.Entry entry : paramMap.entrySet()) {
            List<String> hrefs = (List<String>) entry.getValue();
            hrefs.stream().forEach(href -> {
                try {
                    BidModel bidModel = new BidModel();
                    bidModel.setHref(href);
                    Document document = DataCamling.docCamling(href);
//                    map.put("param", entry.getKey().toString());
                    bidModel.setKeyWord(entry.getKey().toString());
                    //获取招标标题
                    String title = document.getElementsByTag("h2").get(0).text();
//                    map.put("title", title);
                    bidModel.setTitle(title);
                    //获取招标详情
                    Element noticeDetail = document.getElementById("NoticeDetail");
                    if (noticeDetail != null
                            && noticeDetail.getElementsByTag("tbody").size() > 0) {
                        bidModel.setContent(noticeDetail.html());
                        /*Element tbody = noticeDetail.getElementsByTag("tbody").get(0);
                        if (tbody != null) {
                            Elements trs = tbody.getElementsByTag("tr");
                            for (int i = 1; i < trs.size(); i++) {
                                String name = trs.get(i).getElementsByTag("td").get(1).text();
//                                map.put("name", name);
                                bidModel.setName(name);
                                String supplierName = trs.get(i).getElementsByTag("td").get(2).text();
//                                map.put("supplierName", supplierName);
                                bidModel.setSupplierName(supplierName);
                                String costs = trs.get(i).getElementsByTag("td").get(4).text();
//                                map.put("costs", costs);
                                bidModel.setCosts(costs);
                            }*/
                            //获取招标时间
                        Element eDetail = document.getElementsByClass("vF_detail_content").get(0);
                        String detailContent = eDetail.text();
                        if (detailContent.lastIndexOf("成交日期") > 0) {
                            String time1 = detailContent.substring(detailContent.lastIndexOf("成交日期"));
                            time1 = time1.substring(0, 15);
//                                map.put("winningBidTime", time1);
                            bidModel.setTime(time1);
                            time1 = null;
                        } else if (detailContent.lastIndexOf("公告期限") > 0) {
                            String time2 = detailContent.substring(detailContent.lastIndexOf("公告期限"));
                            time2 = time2.substring(0, 30);
//                                map.put("annoPreiodTime", time2);
                            bidModel.setTime(time2);
                            time2 = null;
                        }
//                            maps.add(map);
                    }

                    //另一种页面格式
                    //获取招标标题
                    /*title = document.getElementsByTag("h2").get(0).text();
                    map.put("title", title);
                    //获取时间
                    String pubTime = document.getElementById("pubTime").text();
                    map.put("winningBidTime", pubTime);
                    Elements tbodys = document.getElementsByTag("tbody");
                    if (tbodys.size() > 0){
                        Element last = tbodys.last();
                        Elements trs = last.getElementsByTag("tr");
                        for (int i = 1; i < trs.size(); i++) {
                            Element tr = trs.get(i);
                            String name = tr.getElementsByTag("td").get(0).text();
                            map.put("name", name);
                            String costs = tr.getElementsByTag("td").get(4).text();
                            map.put("costs", costs);
                        }
                        maps.add(map);
                    }*/

                    //第三种页面格式
                    /*title = document.getElementsByTag("h2").get(0).text();
                    //获取时间
                    pubTime = document.getElementById("pubTime").text();
                    Elements tables = document.getElementsByTag("table");
                    Element last = tables.last();

                    Elements trs = last.getElementsByTag("tr");

                    for (int i = 1; i < trs.size(); i = i + 2) {
                        LinkedHashMap map1 = new LinkedHashMap<>();
                        map.put("title", title);
                        map.put("winningBidTime", pubTime);
                        Element tr = trs.get(i);
                        String name = tr.getElementsByTag("td").get(2).text();
                        String amount = tr.getElementsByTag("td").get(5).text();
                        String price = tr.getElementsByTag("td").get(6).text();
                        BigDecimal costs = new BigDecimal(price).multiply(new BigDecimal(amount));
                        map.put("name", name);
                        map.put("costs", costs.setScale(2, RoundingMode.HALF_UP) + "元");
                        maps.add(map1);
                        map1 = null;
                    }*/

                    list.add(bidModel);
                    title = null;
                    bidModel = null;
                } catch (IOException e) {
                    LogUtil.info("CCGP查询详情失败");
                    e.printStackTrace();
                }
            });
        }
        return list;
    }
}
