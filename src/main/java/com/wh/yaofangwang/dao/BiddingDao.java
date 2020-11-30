package com.wh.yaofangwang.dao;

import com.wh.yaofangwang.common.BaseDao;
import com.wh.yaofangwang.common.page.PageUtil;
import com.wh.yaofangwang.model.bid.BidModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BiddingDao
 * @Desc
 * @Author wh
 * @Date 2020/10/14
 */
@Repository
public class BiddingDao extends BaseDao<BidModel, BidModel> {

    public int save(BidModel bidModel, String tableName) {
        StringBuilder strb = new StringBuilder();
        strb.append("insert into " + tableName + "(");
        strb.append("title, href, time, key_word, bid_type, content, create_time");
        strb.append(") values (");
        strb.append(":title, :href, :time, :keyWord, :bidType, :content, now()");
        strb.append(")");
        int count = super.insert(strb.toString(), bidModel);
        return count;
    }

    public List<Map<String, Object>> listFirstBidding(String tableName, List<String> keyWords) {
        StringBuilder strb = new StringBuilder();
        strb.append("select tmp.* from (");
        for (int i = 0; i < keyWords.size(); i++) {
            if (i != 0) strb.append("union all ");
            strb.append("(select bid_type, key_word, href from " + tableName + " ");
            strb.append("where key_word = '" + keyWords.get(i) + "' group by bid_type ) ");
        }
        strb.append(") tmp");
        return super.jdbcTemplate.queryForList(strb.toString());
    }

    public int countInfo(String keyWord, String title, String bidType, String tableName) {

        List<String> params = new ArrayList<>();
        StringBuilder strb = new StringBuilder();
        strb.append("select count(*) from " + tableName + " where 1=1 ");
        if (!StringUtils.isEmpty(keyWord)){
            strb.append("and key_word like ? ");
            params.add("%" + keyWord + "%");
        }
        if (!StringUtils.isEmpty(title)){
            strb.append("and title like ? ");
            params.add("%" + title + "%");
        }
        if (!StringUtils.isEmpty(bidType)){
            strb.append("and bid_type = ? ");
            params.add(bidType);
        }
        return super.count(strb.toString(), params.toArray());
    }

    public List<BidModel> pageInfo(String keyWord, String title, String bidType, String tableName,
                                   int pageIndex, int pageSize) {

        List<String> params = new ArrayList<>();
        StringBuilder strb = new StringBuilder();
        strb.append("select * from " + tableName + " where 1=1 ");
        if (!StringUtils.isEmpty(keyWord)){
            strb.append("and key_word like ? ");
            params.add("%" + keyWord + "%");
        }
        if (!StringUtils.isEmpty(title)){
            strb.append("and title like ? ");
            params.add("%" + title + "%");
        }
        if (!StringUtils.isEmpty(bidType)){
            strb.append("and bid_type = ? ");
            params.add(bidType);
        }
        String sql = PageUtil.createMysqlPageSql(strb.toString(), pageIndex, pageSize);
        return super.list(sql, params.toArray());
    }

    public List<Map<String, Object>> listTableName(){
        StringBuilder strb = new StringBuilder();
        strb.append("select table_name, table_comment from information_schema.tables ");
        strb.append("where table_schema = (select database())");
        return super.jdbcTemplate.queryForList(strb.toString());
    }
}
