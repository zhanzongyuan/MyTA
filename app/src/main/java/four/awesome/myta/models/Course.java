package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by applecz on 2018/1/3.
 */

public class Course implements Serializable {
    private int id;

    @SerializedName("course_name")
    private String courseName;

    @SerializedName("teacher_id")
    private int teacherId;

    @SerializedName("teacher_name")
    private String teacherName;


    private List<Assignment> assignmentList;

    public Course(int id, String name, String teacher) {
        this.id = id;
        assignmentList = new ArrayList<>();
        this.courseName = name;
        this.teacherName = teacher;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.courseName = name;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public void setTeacher(String teacher) {
        this.teacherName = teacher;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return courseName;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public String getTeacher() {
        return teacherName;
    }

}
