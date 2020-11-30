package com.wh.yaofangwang.controller;

import com.wh.yaofangwang.common.JsonResult;
import com.wh.yaofangwang.common.page.PageVO;
import com.wh.yaofangwang.model.bid.BidModel;
import com.wh.yaofangwang.service.BiddingServiceImpl;
import com.wh.yaofangwang.task.AutoCalwerGGZYTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BiddingController
 * @Desc
 * @Author wh
 * @Date 2020/10/13
 */
@RestController
@RequestMapping("/bid")
public class BiddingController {

    @Autowired
    private BiddingServiceImpl biddingService;

    @GetMapping("/beginSearch")
    public String beginSearch(int bidType){
        biddingService.beginSearch(bidType);
        return "成功";
    }

    @GetMapping("/searchCCGPWithDetail")
    public String searchCCGPWithDetail(int bidType) throws IOException {
        biddingService.searchCCGPWithDetail(bidType);
        return "详情成功";
    }

    @GetMapping("/searchJnGGZYWithDetail")
    public String searchJnGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchJnGGZYWithDetail(bidType);
        return "GGZY详情成功";
    }

    @GetMapping("/searchQdGGZYWithDetail")
    public String searchQdGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchQdGGZYWithDetail(bidType);
        return "QDGGZY详情成功";
    }

    @GetMapping("/searchDzGGZYWithDetail")
    public String searchDzGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchDzGGZYWithDetail(bidType);
        return "DZGGZY详情成功";
    }

    @GetMapping("/searchLcGGZYWithDetail")
    public String searchLcGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchLcGGZYWithDetail(bidType);
        return "LCGGZY详情成功";
    }

    @GetMapping("/searchRzGGZYWithDetail")
    public String searchRzGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchRzGGZYWithDetail(bidType);
        return "RZGGZY详情成功";
    }

    @GetMapping("/searchWfGGZYWithDetail")
    public String searchWfGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchWfGGZYWithDetail(bidType);
        return "WFGGZY详情成功";
    }

    @GetMapping("/searchWhGGZYWithDetail")
    public String searchWhGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchWhGGZYWithDetail(bidType);
        return "WHGGZY详情成功";
    }

    @GetMapping("/searchYtGGZYWithDetail")
    public String searchYtGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchYtGGZYWithDetail(bidType);
        return "YTGGZY详情成功";
    }

    @GetMapping("/searchBzGGZYWithDetail")
    public String searchBzGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchBzGGZYWithDetail(bidType);
        return "BZGGZY详情成功";
    }

    @GetMapping("/searchZbGGZYWithDetail")
    public String searchZbGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchZbGGZYWithDetail(bidType);
        return "ZBGGZY详情成功";
    }

    @GetMapping("/searchLyGGZYWithDetail")
    public String searchLyGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchLyGGZYWithDetail(bidType);
        return "LYGGZY详情成功";
    }

    @GetMapping("/searchHzGGZYWithDetail")
    public String searchHzGGZYWithDetail(int bidType) throws IOException {
        biddingService.searchHzGGZYWithDetail(bidType);
        return "HZGGZY详情成功";
    }


    @PostMapping("/pageCCGPInfo")
    public JsonResult pageCCGPInfo(String keyWord, String title, String bidType, String tableName,
                                   int pageIndex, int pageSize){

        PageVO<BidModel> page = biddingService.pageCCGPInfo(keyWord, title, bidType, tableName, pageIndex, pageSize);
        JsonResult jsr = JsonResult.getSuccess_instance();
        jsr.setData(page);
        return jsr;
    }

    @GetMapping("/listTableNames")
    public JsonResult listTableNames(){

        List<Map<String, Object>> maps = biddingService.listTableNames();
        JsonResult jsr = JsonResult.getSuccess_instance();
        jsr.setData(maps);
        return jsr;
    }


    @Autowired
    private AutoCalwerGGZYTask autoCalwerGGZYTask;
    @GetMapping("/runAllTask")
    public void runAllTask(){
        autoCalwerGGZYTask.ggzyTask();
    }

}
