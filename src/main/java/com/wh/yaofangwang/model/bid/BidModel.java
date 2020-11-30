package com.wh.yaofangwang.model.bid;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName BidModel
 * @Desc
 * @Author wh
 * @Date 2020/10/14
 */
public class BidModel {
    private int id;
    private String title;
    private String href;
    private String time;
    private String keyWord;
    private int bidType;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public int getBidType() {
        return bidType;
    }

    public void setBidType(int bidType) {
        this.bidType = bidType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
