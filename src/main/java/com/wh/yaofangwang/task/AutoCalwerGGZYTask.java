package com.wh.yaofangwang.task;

import com.wh.yaofangwang.service.BiddingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ClassName AutoCalwerGGZYTask
 * @Desc
 * @Author wh
 * @Date 2020/10/26
 */
@Component
public class AutoCalwerGGZYTask {

    @Autowired
    private BiddingServiceImpl biddingService;

    @Scheduled(cron = "0 0 0 * * *")
    public void ggzyTask() {

        for (int i = 1; i <= 13; i++) {
            CCGPRunabled runabled = new CCGPRunabled(i, biddingService);
            Thread thread = new Thread(runabled);
            thread.setName("task" + i);
            thread.start();
        }
    }

}

class CCGPRunabled implements Runnable {

    private BiddingServiceImpl biddingService = null;
    private Integer flag = null;
    public CCGPRunabled() {
    }

    public CCGPRunabled(Integer flag, BiddingServiceImpl biddingService) {
        this.flag = flag;
        this.biddingService = biddingService;
    }

    @Override
    public void run() {
        try {
            switch (flag) {
                case 1: {
                    biddingService.searchCCGPWithDetail(1);
                    biddingService.searchCCGPWithDetail(7);
                } break;
                case 2: {
                    biddingService.searchJnGGZYWithDetail(1);
                    biddingService.searchJnGGZYWithDetail(7);
                } break;
                case 3: {
                    biddingService.searchQdGGZYWithDetail(0);
                } break;
                case 4: {
                    biddingService.searchDzGGZYWithDetail(0);
                } break;
                case 5: {
                    biddingService.searchLcGGZYWithDetail(0);
                } break;
                case 6: {
                    biddingService.searchRzGGZYWithDetail(0);
                } break;
                case 7: {
                    biddingService.searchWfGGZYWithDetail(0);
                } break;
                case 8: {
                    biddingService.searchWhGGZYWithDetail(1);
                    biddingService.searchWhGGZYWithDetail(7);
                } break;
                case 9: {
                    biddingService.searchYtGGZYWithDetail(0);
                } break;
                case 10: {
                    biddingService.searchBzGGZYWithDetail(0);
                } break;
                case 11: {
                    biddingService.searchZbGGZYWithDetail(0);
                } break;
                case 12: {
                    biddingService.searchLyGGZYWithDetail(0);
                } break;
                case 13: {
                    biddingService.searchHzGGZYWithDetail(0);
                } break;

                default: break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}