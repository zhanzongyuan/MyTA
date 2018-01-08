package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.eventbus.Subscribe;

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
    @SerializedName("course_id")
    private int courseId;
    private User creator;
    @SerializedName("id")
    private int assignId;
    private int teacherId;
    public Assignment(String name, Date startTime, Date endTime, String detail, User creator, int assignId) {
        this.name = name;
        this.endTime = endTime;
        this.startTime = startTime;
        this.detail = detail;
        this.creator = creator;
        this.assignId = assignId;
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
    public void setAssignId(int assignId) {this.assignId = assignId; }
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
    public int getAssignId() {return assignId;}
    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }
    public int getCourseId() {
        return courseId;
    }
}
