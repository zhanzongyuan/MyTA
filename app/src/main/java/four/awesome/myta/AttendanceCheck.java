package four.awesome.myta;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.List;

import four.awesome.myta.models.Attendance;
import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class AttendanceCheck extends AppCompatActivity implements Observer<Response<Attendance>> {
    private Button start_stop_att;
    private ListView att_result;
    private ArrayList<String> att_result_name = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private EditText att_input;
    private TextView att_or_time;
    private TextView rand_code;
    private boolean if_start_att = false;
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
        initialView();
    }

    private void initialData() {
        initialHandler();
        context = this;
        observer = this;
        Intent intent = getIntent();
        apiKey = intent.getStringExtra("apiKey");
        user_type = intent.getStringExtra("type");
        user_id = intent.getIntExtra("userId", -1);
        course_id = intent.getIntExtra("courseId", -1);
        att_result_name.add("张涵玮");
        att_result_name.add("张家侨");

        // Change theme when different user.
        if (user_type.equals("teacher")) {
            setTheme(R.style.AppThemeTA);
        }
    }

    private void initialView() {
        att_input = (EditText) findViewById(R.id.att_input);
        rand_code = (TextView) findViewById(R.id.show_rand_num);
        att_or_time = (TextView) findViewById(R.id.att_or_time);
        start_stop_att = (Button) findViewById(R.id.start_stop_attendance);
        att_result = (ListView) findViewById(R.id.show_att_result);
        final LinearLayout stu_att = (LinearLayout) findViewById(R.id.stu_att);

        adapter = new ArrayAdapter<String>(this, R.layout.attendance_result, R.id.att_result_name, att_result_name);
        att_result.setAdapter(adapter);

        if (user_type.equals("student")) {
            att_or_time.setText("签到码");
            start_stop_att.setText("签到");
            att_result.setVisibility(View.INVISIBLE);
            start_stop_att.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String input = att_input.getText().toString();
                    start_stop_att.setClickable(false);
                    start_stop_att.setTextColor(getResources().getColor(R.color.gray));
                }
            });

        } else {
            att_or_time.setText("时间：");
            start_stop_att.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("OK");
                    String last = att_input.getText().toString();
                    if (last.equals("")) {
                        Toast.makeText(context, "时间不能为空", Toast.LENGTH_LONG).show();
                    } else {
                        start_stop_att.setClickable(false);
                        start_stop_att.setTextColor(getResources().getColor(R.color.gray));
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
                        // TODO: 18-1-7 请后端修改获取签到对象的接口 
                        (new APIClient()).subscribeGetAttendanceCode(observer, course_id, attendance.getId(), apiKey);
                        start_stop_att.setClickable(true);
                        start_stop_att.setTextColor(getResources().getColor(R.color.black));
                        break;
                    case 111:
                        System.out.println("Thread renew!");
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
            }
        } else if (res.code() == 200) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {}
}
