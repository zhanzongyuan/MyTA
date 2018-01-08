package four.awesome.myta;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import four.awesome.myta.models.Attendance;
import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class AttendanceCheck extends AppCompatActivity implements Observer<Response<Attendance>> {
    private Button start_stop_att;
    private ListView att_result;
    private ArrayList<Integer> att_result_id = new ArrayList<Integer>();
    private ArrayList<String> att_result_name = new ArrayList<String>();
    private List<User> stu_list = new LinkedList<User>();
    //private ArrayAdapter<String> adapter;

    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> data = new ArrayList<>();

    private EditText att_input;
    private TextView att_or_time;
    private TextView rand_code;
    private TextView attendance_string;
    private String user_type;
    private String apiKey;
    private Attendance attendance;
    private int course_id;
    private int user_id;
    private Observer<Response<Attendance>> observer;
    private Context context;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialData();
        setContentView(R.layout.activity_attendance_check);
        (getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initialView();
        initialHandler();
    }

    private void initialData() {
        System.out.println("initial data ok1");
        context = this;
        observer = this;
        Intent intent = getIntent();
        apiKey = intent.getStringExtra("apiKey");
        user_type = intent.getStringExtra("type");
        user_id = intent.getIntExtra("userId", -1);
        course_id = intent.getIntExtra("courseId", -1);
        System.out.println("initial data ok2");
        (new APIClient()).subscribeGetUsers(new Observer<Response<List<User>>>() {
            @Override
            public void onSubscribe(Disposable d) {}
            @Override
            public void onNext(Response<List<User>> res) {
                if (res.code() == 200) {
                    stu_list.addAll(res.body());
                    System.out.println("stu_list = " + stu_list.size());
                }
            }

            @Override
            public void onError(Throwable e) {e.printStackTrace();}

            @Override
            public void onComplete() {}
        }, apiKey, course_id, "student");

        if (user_type.equals("teacher")) {
            setTheme(R.style.AppThemeTA);
        }
    }

    private void initialView() {
        attendance_string = (TextView) findViewById(R.id.attendance_string);
        att_input = (EditText) findViewById(R.id.att_input);
        rand_code = (TextView) findViewById(R.id.show_rand_num);
        att_or_time = (TextView) findViewById(R.id.att_or_time);
        start_stop_att = (Button) findViewById(R.id.start_stop_attendance);
        att_result = (ListView) findViewById(R.id.show_att_result);
        final LinearLayout stu_att = (LinearLayout) findViewById(R.id.stu_att);

        Map<String, Object> temp = new LinkedHashMap<>();
        temp.put("name", "姓名");
        temp.put("stu_id", "学号");
        temp.put("state", "状态");
        data.add(temp);

        simpleAdapter = new SimpleAdapter(this, data, R.layout.attendance_result, new String[] {"name", "stu_id", "state"}, new int[] {R.id.att_result_name, R.id.stu_id, R.id.state});

        //adapter = new ArrayAdapter<String>(this, R.layout.attendance_result, R.id.att_result_name, att_result_name);
        att_result.setAdapter(simpleAdapter);

        if (user_type.equals("student")) {
            att_or_time.setText("签到码:");
            start_stop_att.setText("签到");
            att_result.setVisibility(View.INVISIBLE);
            attendance_string.setVisibility(View.INVISIBLE);
            rand_code.setVisibility(View.INVISIBLE);
            start_stop_att.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = att_input.getText().toString();
                    start_stop_att.setClickable(false);
                    start_stop_att.setTextColor(getResources().getColor(R.color.gray));
                    (new APIClient()).subscribeCallAttendance(observer, user_id, code, apiKey);
                }
            });

        } else {
            att_or_time.setText("时间：");
            start_stop_att.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("OK");
                    String last = att_input.getText().toString();
                    int last_i = Integer.parseInt(last);
                    if (last.equals("") || last_i == 0) {
                        Toast.makeText(context, "时间不能为空或0", Toast.LENGTH_LONG).show();
                    } else {
                        start_stop_att.setClickable(false);
                        start_stop_att.setTextColor(getResources().getColor(R.color.gray));
                        att_input.setFocusable(false);
                        att_input.setEnabled(false);
                        (new APIClient()).subscribeCreateAttendanceCheck(observer, course_id,  Integer.parseInt(last), apiKey);
                        createThread(Integer.parseInt(last));
                    }
                }
            });
        }
    }

    private void initialHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 123:
                        (new APIClient()).subscribeGetAttendanceCode(observer, course_id, attendance.getId(), apiKey);
                        break;
                    case 111:
                        String time = att_input.getText().toString();
                        int time_i = Integer.parseInt(time);
                        time_i = time_i - 1;
                        att_input.setText("" + time_i);
                }
            }
        };
    }

    private void createThread(int last) {
        final int milliLast = last * 1000;
        Thread thread = new Thread() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                while(System.currentTimeMillis() - time <= milliLast) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.obtainMessage(111).sendToTarget();
                }
                handler.obtainMessage(123).sendToTarget();
            }
        };
        thread.start();
    }

    private void addStudentNameToAdapter() {
        for (int i = 0; i < stu_list.size(); i++) {
            boolean flag = true;
            Map<String, Object> temp = new LinkedHashMap<>();
            for (int j = 0; j < att_result_id.size(); j++) {
                if (att_result_id.get(j) == stu_list.get(i).getID()) {
                    att_result_name.add(stu_list.get(i).getName());
                    temp.put("name", stu_list.get(i).getName());
                    temp.put("stu_id", stu_list.get(i).getCampusID());
                    temp.put("state", "已签到");
                    flag = false;
                    break;
                }
            }
            if (flag) {
                temp.put("name", stu_list.get(i).getName());
                temp.put("stu_id", stu_list.get(i).getCampusID());
                temp.put("state", "未签到");
            }
            data.add(temp);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {}

    @Override
    public void onNext(Response<Attendance> res) {
        System.out.println("res code = " + res.code());
        if (res.code() == 201) {
            if (user_type.equals("teacher")) {
                attendance = res.body();
                if (attendance == null) {
                    return;
                }
                rand_code.setText(attendance.getCode());
            } else {
                Toast.makeText(context, "签到成功", Toast.LENGTH_LONG).show();
            }
        } else if (res.code() == 200) {
            attendance = res.body();
            if (attendance == null) {
                return;
            }
            att_result_id.addAll(attendance.getStudentList());
            addStudentNameToAdapter();
            System.out.println("id size = " + att_result_id.size());
            simpleAdapter.notifyDataSetChanged();
        } else {
            if (user_type.equals("teacher")) {
                Toast.makeText(context, "发起签到失败", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "签到失败", Toast.LENGTH_LONG).show();
            }
            start_stop_att.setClickable(true);
            att_input.setFocusable(true);
            att_input.setEnabled(true);
            start_stop_att.setTextColor(getResources().getColor(R.color.black));
        }
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
