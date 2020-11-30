package com.wh.yaofangwang.service;

import cn.hutool.core.map.MapUtil;
import com.wh.yaofangwang.common.Constants;
import com.wh.yaofangwang.common.page.PageVO;
import com.wh.yaofangwang.dao.BiddingDao;
import com.wh.yaofangwang.model.bid.BidModel;
import com.wh.yaofangwang.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @ClassName BiddingService
 * @Desc
 * @Author wh
 * @Date 2020/10/13
 */
@Service
@Transactional
public class BiddingServiceImpl {
    Logger logger= LoggerFactory.getLogger(BiddingServiceImpl.class);

    @Autowired
    private BiddingDao biddingDao;

    public Map<String, List<String>> listBaseData(String tableName){
        Map<String, List<String>> resultMap = new HashMap<>();

        List<Map<String, Object>> maps = biddingDao.listFirstBidding(tableName, Arrays.asList(Constants.params));
        maps.stream().forEach(map -> {

            //如果结果集中含有关键字 这取出关键字中的集合 添加新值
            //否则新建关键字 插入新值
            if (resultMap.keySet().contains(map.get("KEY_WORD"))) {
                for (Map.Entry<String, List<String>> entry: resultMap.entrySet()) {
                    if (map.get("KEY_WORD").equals(entry.getKey())) {
                        List<String> tmpList = entry.getValue();
                        tmpList.add((String) map.get("HREF"));
                        resultMap.put((String) map.get("KEY_WORD"), tmpList);
                    }
                }
            } else {
                List<String> tmpList = new ArrayList<>();
                tmpList.add((String) map.get("HREF"));
                resultMap.put((String) map.get("KEY_WORD"), tmpList);
            }

        });
        return resultMap;
    }

    //抓取 中国政府采购网 数据 带详情
    public void searchCCGPWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_CCGP);
        LinkedHashMap<String, List<String>> listMap = SearchCCGPUtil.searchListLoop(String.valueOf(bidType), resultMap);
        if (bidType == 1) { //招标
            list = SearchCCGPUtil.searchOpenTenderDetail(listMap);
            list.stream().forEach(bidModel -> {
                bidModel.setBidType(bidType);
                biddingDao.save(bidModel, Constants.TABLE_NAME_CCGP);
            });
        } else if (bidType == 7) { //中标
            list = SearchCCGPUtil.searchWinningBidDetail(listMap);
            list.stream().forEach(bidModel -> {
                bidModel.setBidType(bidType);
                biddingDao.save(bidModel, Constants.TABLE_NAME_CCGP);
            });
        }
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchJnGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_JN);
        LinkedHashMap<String, List<String>> listMap = SearchJnGgzyUtil.searchGgzyjyzxLoop(bidType, resultMap);
        list = SearchJnGgzyUtil.searchGgzyjyzxDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(bidType);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_JN);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchQdGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_QD);
        LinkedHashMap<String, List<String>> listMap = SearchQdGGZYUtil.searchQdGgzyLoop(bidType,resultMap);
        list = SearchQdGGZYUtil.searchQdGgzyDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(bidType);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_QD);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchDzGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_DZ);
        LinkedHashMap<String, List<String>> listMap = SearchDzGGZYUtil.searchDzGGZYLoop(bidType, resultMap);
        list = SearchDzGGZYUtil.searchDzGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(bidType);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_DZ);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchLcGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_LC);
        LinkedHashMap<String, List<String>> listMap = SearchLcGGZYUtil.searchLCGGZYLoop(bidType, resultMap);
        list = SearchLcGGZYUtil.searchLCGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_LC);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchRzGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_RZ);
        LinkedHashMap<String, List<String>> listMap = SearchRzGGZYUtil.searchRZGGZYLoop(bidType, resultMap);
        list = SearchRzGGZYUtil.searchRZGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_RZ);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchWfGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_WF);
        LinkedHashMap<String, List<String>> listMap = SearchWfGGZYUtil.searchWFGGZYLoop(bidType, resultMap);
        list = SearchWfGGZYUtil.searchWFGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_WF);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchWhGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_WH);
        LinkedHashMap<String, LinkedHashSet<String>> listMap = SearchWhGGZYUtil.searchWHGGZYLoop(bidType, resultMap);
        list = SearchWhGGZYUtil.searchWHGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(bidType);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_WH);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchYtGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_YT);
        LinkedHashMap<String, List<String>> listMap = SearchYtGGZYUtil.searchYTGgzyLoop(bidType, resultMap);
        list = SearchYtGGZYUtil.searchYTGgzyDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(0);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_YT);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchBzGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_BZ);
        LinkedHashMap<String, List<String>> listMap = SearchBzGGZYUtil.searchBZGGZYLoop(bidType, resultMap);
        list = SearchBzGGZYUtil.searchBZGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(0);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_BZ);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchZbGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_ZB);
        LinkedHashMap<String, List<String>> listMap = SearchZbGGZYUtil.searchZBGGZYLoop(bidType, resultMap);
        list = SearchZbGGZYUtil.searchZBGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(0);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_ZB);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchLyGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_LY);
        LinkedHashMap<String, List<String>> listMap = SearchLyGGZYUtil.searchLYGGZYLoop(bidType, resultMap);
        list = SearchLyGGZYUtil.searchLYGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(0);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_LY);
        });
    }
    //抓取 济南公共资源交易中心网 带详情
    public void searchHzGGZYWithDetail(int bidType) throws IOException {
        List<BidModel> list = null;
        Map<String, List<String>> resultMap = this.listBaseData(Constants.TABLE_NAME_GGZY_HZ);
        LinkedHashMap<String, List<String>> listMap = SearchHzGGZYUtil.searchHZGGZYLoop(bidType, resultMap);
        list = SearchHzGGZYUtil.searchHZGGZYDetail(listMap);
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(0);
            String title = bidModel.getTitle();
            if (StringUtils.isEmpty(title)){
                title = "标题未抓取成功";
                bidModel.setTitle(title);
            }
            biddingDao.save(bidModel, Constants.TABLE_NAME_GGZY_HZ);
        });
    }


    //抓取 中国政府采购网 数据
    public void beginSearch(int bidType) {

        List<BidModel> list = SearchCCGPUtil.searchListLoopWeb(String.valueOf(bidType));
        list.stream().forEach(bidModel -> {
            bidModel.setBidType(bidType);
            biddingDao.save(bidModel, Constants.TABLE_NAME_CCGP);
        });
    }

    public PageVO<BidModel> pageCCGPInfo(String keyWord, String title, String bidType, String tableName,
                                     int pageIndex, int pageSize){
        PageVO<BidModel> page = new PageVO<>();
        int count = biddingDao.countInfo(keyWord, title, bidType, tableName);
        page.setCount(count);
        List<BidModel> list = biddingDao.pageInfo(keyWord, title, bidType, tableName, pageIndex, pageSize);
        page.setRecords(list);
        page.setPageSize(pageSize);
        page.setCurrentPageNum(pageIndex);
        return page;
    }

    public List<Map<String, Object>> listTableNames(){
        List<Map<String, Object>> tableNames = new ArrayList<>();
        List<Map<String, Object>> maps = biddingDao.listTableName();
        maps.stream()
                .filter(map -> {
                    String tableName = (String) map.get("TABLE_NAME");
                    if (tableName.contains("copy")) return false;
                    return true;
                })
                .forEach(map -> {
                    map.put((String) map.get("TABLE_NAME"), map.get("TABLE_COMMENT"));
                    map = MapUtil.toCamelCaseMap(map);
                    tableNames.add(map);
                });

        return tableNames;
    }

}
