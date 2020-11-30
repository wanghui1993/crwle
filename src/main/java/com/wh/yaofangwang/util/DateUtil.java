package com.wh.yaofangwang.util;

import com.wh.yaofangwang.common.LogUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DateUtil
 * @Desc
 * @Author wh
 * @Date 2020/10/15
 */
public class DateUtil {

    public static Map<String, String> calaDate(int bidType, String delimiter) {

        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        int month = ca.get(Calendar.MONTH) + 1;
        String endTime = ca.get(Calendar.YEAR) + delimiter + month + delimiter + ca.get(Calendar.DAY_OF_MONTH);;
        if (bidType == 1) {
            //中标最近一个月
            ca.add(Calendar.MONTH, -3);
            month = ca.get(Calendar.MONTH) + 1;

        } else if (bidType == 7) {
            //中标两年内
            ca.add(Calendar.YEAR, -1);
        }
        String startTime = ca.get(Calendar.YEAR) + delimiter + month + delimiter + ca.get(Calendar.DAY_OF_MONTH);
        Map<String, String> map = new HashMap();
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }
}
