
package com.wh.yaofangwang.common;

import com.mysql.jdbc.StringUtils;

import java.util.HashMap;

/**
 * 所有Rest接口返回公共对象
 *
 */
public class JsonResult {
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 消息
     */
    private String message;
    /**
     * 返回码
     */
    private String code;

    /**
     * 额外的数据
     */
    private Object data;

    public String getCode() {
        return code;
    }

    public JsonResult setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data == null ? new HashMap() : data;
    }

    public JsonResult setData(Object data) {
        this.data = data;
        return this;
    }

    public JsonResult() {
    }

    public JsonResult(boolean success) {
        this.success = success;
    }

    /*public JsonResult setEnum(GlobalReturnCode e) {
        this.code = e.getCode();
        this.message = e.getMessage();
        return this;
    }*/

    /**
     * 三个参数，message自己赋值，不通过code获取
     *
     * @param success
     * @param code
     * @param message
     */
    public JsonResult(boolean success, String code, String message) {
        this(success);
        this.code = code;
        this.message = message;
    }

    public JsonResult(boolean success, String code) {
        this(success);
        this.code = code;
        String msg = "";//ReturnCodeUtil.getMsg(code);
        this.message = StringUtils.isNullOrEmpty(msg) ? code : msg;
    }

    public JsonResult(boolean success, String code, Object data) {
        this(success, code);
        this.data = data;
    }


    public static JsonResult getSuccess_instance() {
        return new JsonResult(true,"S10003","操作成功");
    }

    public static JsonResult getError_instance() {
        return new JsonResult(false,"E30002","操作失败");
    }

    public JsonResult toSuccess() {
        this.success = true;
//        setEnum(GlobalReturnCode.SYSTEM_OPERA_SUCCESS);
        return this;
    }

    @Override
    public String toString() {
        return "JsonResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

