package com.wh.yaofangwang.common;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataCamling {

    static Logger logger = LoggerFactory.getLogger(DataCamling.class);

//    public static int timeOut = 60000;
    public static int timeOut = 10000;
    public String geneCode = null;

    public static final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.92 Safari/537.36";
    public DataCamling() {
    }

    /*
     * @author  wanghui
     * @desc 通过url获取document
     * @data 2018/7/8 11:53
     * @param url
     * @result Document
     */
    public static Document docCamling(String url) throws IOException {

        if (StringUtils.isEmpty(url)){
            logger.error("请求路径不可为空");
            throw new RuntimeException("请求路径不可为空");
        }
        // 使用jsoup向服务器发送请求

        Document document  = Jsoup.connect(url).userAgent(userAgent).timeout(timeOut).get();
        return document;
    }

    /*
     * @author wh
     * @desc   jsoup通过url获取response
     * @date   2018/8/2 14:02
     * @method docCamlingExecute
     * @param  [url]
     * @result org.jsoup.Connection.Response
     */
    public static Connection.Response docCamlingExecute(String url) throws IOException {

        if (StringUtils.isEmpty(url)){
            logger.error("请求路径不可为空");
            throw new RuntimeException("请求路径不可为空");
        }
        // 使用jsoup向服务器发送请求
        Connection.Response execute = Jsoup.connect(url)
                .userAgent(userAgent)
                .timeout(timeOut)
                .ignoreContentType(true)
                .execute();
        return execute;
    }

    /*
     * @author wh
     * @desc   jsoup通过url获取response
     * @date   2018/8/2 14:02
     * @method docCamlingExecute
     * @param  [url]
     * @result org.jsoup.Connection.Response
     */
    /*public static String docCamlingProxyExecute(String url,String httpProxyHost,int httpProxyPort) throws IOException {
        return HttpUtil.getJsonFromUrlProxy(url);
    }*/



    /*
     * @author  wanghui
     * @desc 通过url集合返回document
     * @data 2018/7/8 18:20
     * @param geneCode
     * @result List<Document>
     */
    @Deprecated
    public List<Document> docCamlings(List<String> urls){

        List<Document> documents = new ArrayList<>();
        if (urls == null || urls.size() <= 0){
            throw new ExceptionInInitializerError("请配置访问路径");
        }

        try {
            Document document = null;
            for (String url : urls) {
                // 使用jsoup向服务器发送请求
                String UA = "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50";
                document = Jsoup.connect(url).timeout(timeOut).userAgent(UA).validateTLSCertificates(false).get();
                documents.add(document);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return documents;
    }

    /*
     * @author  wanghui
     * @desc 文件写入 默认路径为E:/temp
     * @data 2018/7/9 14:13
     * @param document
     * @result boolean
     */
    public static boolean writeFile(Document document, String filePath){

        try {
            File file = new File(filePath);
            if (!file.exists()){
                file.mkdirs();
            }
            filePath = filePath + System.currentTimeMillis() + ".txt";
            BufferedWriter bw = null;
            FileWriter fileWriter = new FileWriter(filePath);
            bw = new BufferedWriter(fileWriter);
            bw.write(document.toString());
            bw.close();
            return true;
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    /*
     * @author  wanghui
     * @desc 文件写入
     * @data 2018/7/9 14:13
     * @param params
     * @result boolean
     */
    public static boolean writeFile(String params, String filePath){

        try {
            /*File file = new File(filePath);
            if (!file.exists()){
                file.mkdirs();
            }*/
//            filePath = filePath + System.currentTimeMillis() + ".txt";
//            filePath = filePath + ".txt";
            BufferedWriter bw = null;
            FileWriter fileWriter = new FileWriter(filePath, true);
            bw = new BufferedWriter(fileWriter);
            bw.write(params);
            bw.newLine();
            bw.close();
            return true;
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return false;
    }

    /*
     * @author  wh
     * @desc 从磁盘读文件
     * @date 2018/7/11 10:56
     * @method readFile
     * @param fileName
     * @result String
     */
    public static String readFile(String filePath) throws IOException {

        StringBuffer strB = new StringBuffer();
        File file = new File(filePath);
        if (!(file.isFile() && file.exists())){
            throw new IOException("文件不存在");
        }

        String line = null;
        FileReader reader = null;
        BufferedReader bReader = null;
        try {
            reader = new FileReader(file);
            bReader = new BufferedReader(reader);
            while ((line = bReader.readLine()) != null){
                strB.append(line + "\n");
            }
        } finally {
            if (reader != null){
                reader.close();
                bReader.close();
            }

        }
        return strB.toString();
    }

    /*
     * @author  wanghui
     * @desc 路径生成 从配置文件中获取url并拼接相应参数
     * @data 2018/7/9 14:11
     * @method pathGeneration
     * @param urls, geneCode
     * @result List<String>
     */
    @Deprecated
    public List<String> pathGeneration(List<String> urls, String geneCode){
        List<String> urlStrs = new ArrayList<>();
        this.geneCode = geneCode;
        for (String url: urls){
            if (!url.contains("${param}")) continue;

            String replace = url.replace("${param}", geneCode);
            urlStrs.add(replace);
        }
        return urlStrs;
    }

    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    public static Document post(String url, Map<String, String> params) {
        Document post = null;
        try {
            post = Jsoup.connect(url).data(params).timeout(0).post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * post请求 requestBody请求
     * @param url
     * @param params
     * @return
     */
    public static String postWithRequest(String url, String params) {
        String post = null;
        try {
            post = Jsoup.connect(url)
                    .requestBody(params)
                    .timeout(0)
                    .method(Connection.Method.POST)
                    .execute()
                    .body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

}
