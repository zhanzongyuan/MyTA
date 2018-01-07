package four.awesome.myta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.List;

public class AttendanceCheck extends AppCompatActivity {
    private Button start_stop_att;
    private ListView att_result;
    private ArrayList<String> att_result_name = new ArrayList<String>();
    private EditText stu_att_input;
    private boolean if_start_att = false;
    private String user_type;
    private int course_id;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialData();
        setContentView(R.layout.activity_attendance_check);
        initialView();
    }

    private void initialData() {
        Intent intent = getIntent();
        user_type = intent.getStringExtra("type");
        user_id = intent.getIntExtra("userId", -1);
        course_id = intent.getIntExtra("couseId", -1);
        att_result_name.add("张涵玮");
        att_result_name.add("张家侨");

        // Change theme when different user.
        if (user_type.equals("teacher")) {
            setTheme(R.style.AppThemeTA);
        }
    }

    private void initialView() {
        start_stop_att = (Button) findViewById(R.id.start_stop_attendance);
        att_result = (ListView) findViewById(R.id.show_att_result);
        LinearLayout stu_att = (LinearLayout) findViewById(R.id.stu_att);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.attendance_result, R.id.att_result_name, att_result_name);
        att_result.setAdapter(adapter);

        if (user_type.equals("student")) {
            start_stop_att.setText("签到");
            att_result.setVisibility(View.INVISIBLE);
            stu_att_input = (EditText) findViewById(R.id.stu_att_input);

            start_stop_att.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = stu_att_input.getText().toString();
                    // TODO: 18-1-7 学生发送签到码，学生id和课程ID给后端
                }
            });

        } else {
            stu_att.setVisibility(View.INVISIBLE);
            start_stop_att.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!if_start_att) {
                        start_stop_att.setText("结束签到");
                        if_start_att = true;
                        // TODO: 18-1-7 老师发送签到需求给后端请求签到码。
                    } else {
                        if_start_att = false;
                        start_stop_att.setText("开始签到");
                        //结果名单为att_resutl_name列表
                        // TODO: 18-1-7 老师结束签到，后端返回仍然未签到同学的名单
                    }
                }
            });
        }
    }
}
