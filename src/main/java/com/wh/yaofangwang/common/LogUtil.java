
package com.wh.yaofangwang.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class LogUtil {
    /**
     * sys日志
     */
    private static Logger sysLog = LoggerFactory.getLogger("sysLog");
    /**
     * http请求日志
     */
    private static Logger httpLog = LoggerFactory.getLogger("httpLog");

    /**
     * 消息发送日志
     */
    private static Logger smsLog = LoggerFactory.getLogger("smsLog");

    /**
     * cat监控
     */
    private static Logger catLog = LoggerFactory.getLogger("catLog");

    /**
     *  elk监控
     */
    private static Logger elkLog = LoggerFactory.getLogger("elkLog");

    /**
     * 功能描述:记录系统日志
     *
     * @param str 要输出的字符串
     */
    public static void info(String str) {
        sysLog.info(str);
//        elkLog.info(str);
    }

    /**
     * 功能描述:根据指定的格式和参数记录系统日志
     *
     * @param str 要输出的字符串
     * @param arguments a list of 3 or more arguments
     */
    public static void infoByArgs(String str, Object... arguments) {
        sysLog.info(str, arguments);
//        elkLog.info(str, arguments);
    }

    public static void info(Object... objs) {
        StringBuilder sb = new StringBuilder();
        for (Object obj:objs){
            sb.append(obj).append(" ");
        }
        sysLog.info(sb.toString());
//        elkLog.info(sb.toString());
    }

    public static void debug(String str) {
        sysLog .debug(str);
    }

    /**
     * 功能描述:记录系统异常
     *
     * @param str 要输出的字符串
     */
    public static void error(String str) {
        sysLog.error(str);
//        catLog.error(str);
//        elkLog.error(str);
    }

    /**
     * 功能描述:记录系统异常
     *
     * @param e 系统异常
     */
    public static void error(Exception e) {
        sysLog.error(e.getStackTrace().toString());
//        catLog.error(e.getStackTrace().toString());
//        elkLog.error(e.getStackTrace().toString());
    }

    public static void error(String str,Exception e) {
        sysLog.error(str,e);
//        catLog.error(str,e);
//        elkLog.error(str,e);
    }

    /**
     * 功能描述:记录请求的http
     *
     * @param str 要输出的字符串
     */
    public static void infoHttp(String str) {
        httpLog.info(str);
    }


    /**
     * 功能描述:短信发送日志
     *
     * @param str 要输出的字符串
     */
    public static void infoSms(String str) {
        smsLog.info(str);
    }

    /**
     *  功能描述:elk日志
     */
    public static void infoElk(String str) {
        elkLog.info(str);
    }

}
