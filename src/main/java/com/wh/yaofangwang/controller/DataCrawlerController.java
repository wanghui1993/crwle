package com.wh.yaofangwang.controller;

import com.wh.yaofangwang.service.DataCrawlerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DataClawer
 * @Description
 * @Author wh
 * @Date 2020/6/3 9:36
 */
@RestController
@RequestMapping("/dataClawer")
public class DataCrawlerController {

    @Autowired
    private DataCrawlerServiceImpl dataCrawlerService;

    @GetMapping("/writeFile")
    public String writeFile(){
        dataCrawlerService.writeFile();
        return "完成";
    }

    @GetMapping("/writeFileThread")
    public String writeFileThread(){
        dataCrawlerService.writeFileThread();
        return "完成";
    }

    @GetMapping("/crawlerYaofangwangResp")
    public String crawlerYaofangwangResp(){
        String s = dataCrawlerService.crawlerYaofangwangResp();
        return s;
    }
}
