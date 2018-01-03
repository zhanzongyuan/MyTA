package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 丁丁 on 2017/12/27 0027.
 * 一个Assignment包含属性名字name，开始时间publishTime，结束时间endTime，详情detail，创建者creator
 */

public class Assignment implements Serializable {
    @SerializedName("ass_name")
    private String name;
    // 用Date表示日期，具体到年月日，从1900年算起，Date date1=new Date(2015-1900,11,30,23,59,59) 2015年11月30日
    @SerializedName("end_time")
    private Date endTime;
    @SerializedName("publish_time")
    private Date startTime;
    private String detail;
    private User creator;

    public Assignment() {
        name = "";
        endTime = new Date();
        startTime = new Date();
        detail = "";
        creator = new User();
        creator.setName("张涵玮");
        creator.setEmail("123@qq.com");
        creator.setType("teacher");
        creator.setCampusID("12345");
    }
    public Assignment(String name, Date startTime, Date endTime, String detail, User creator) {
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
        this.detail = detail;
        this.creator = creator;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setEndTime (Date endTime) {
        this.endTime = endTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
    public String getName() {
        return name;
    }
    public Date getStartTime() {
        return startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public String getDetail() {
        return detail;
    }
    public User getCreator() {
        return creator;
    }
}
