package four.awesome.myta.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by applecz on 2018/1/3.
 */

public class Course implements Serializable {
    private int id;
    private String name;
    private String teacher;
    private List<Assignment> assignmentList;

    public Course(int id, String name, String teacher) {
        this.id = id;
        assignmentList = new ArrayList<>();
        this.name = name;
        this.teacher = teacher;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssignmentList(List<Assignment> assignmentList) {
        this.assignmentList = assignmentList;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Assignment> getAssignmentList() {
        return assignmentList;
    }

    public String getTeacher() {
        return teacher;
    }
}
