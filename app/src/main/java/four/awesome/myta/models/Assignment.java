package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by 丁丁 on 2017/12/27 0027.
 * 一个Assignment包含属性名字name，开始时间publishTime，结束时间endTime，详情detail，创建者creator
 */

public class Assignment {
    @SerializedName("ass_name")
    private String name;
    // 用Date表示日期，具体到年月日，从1900          年算起，Date date1=new Date(2015-1900,11,30,23,59,59) 2015年11月30日
    @SerializedName("end_time")
    private Date endTime;
    @SerializedName("publish_time")
    private Date publishTime;
    private String detail;
    private String creator;

    public Assignment() {
        name = "";
        endTime = new Date();
        publishTime = new Date();
        detail = "";
        creator = "";
    }
    public Assignment(String name, Date publishTime, Date endTime, String detail, String creator) {
        this.name = name;
        this.endTime = endTime;
        this.publishTime = publishTime;
        this.detail = detail;
        this.creator = creator;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setEndTime (Date endTime) {
        this.endTime = endTime;
    }
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getName() {
        return name;
    }
    public Date getPublishTime() {
        return publishTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public String getDetail() {
        return detail;
    }
    public String getCreator() {
        return creator;
    }
}
