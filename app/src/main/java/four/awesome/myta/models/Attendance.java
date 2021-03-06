package four.awesome.myta.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjiaqiao on 18-1-7.
 */

public class Attendance implements Serializable {
    @SerializedName("id")
    int attendance_id;
    @SerializedName("course_id")
    int course_id;
    @SerializedName("start_time")
    String start_time;
    @SerializedName("last")
    int last;
    @SerializedName("code")
    String code;
    ArrayList<Integer> students;

    public Attendance(int id, int courseId, String s_time, int last_time, String att_code) {
        attendance_id = id;
        course_id = courseId;
        start_time = s_time;
        last = last_time;
        code = att_code;
    }

    public void setStudent(ArrayList<Integer> stu_list) {
        this.students = new ArrayList<Integer>();
        this.students = stu_list;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return attendance_id;
    }

    public ArrayList<Integer> getStudentList() {
        return students;
    }
}
