package four.awesome.myta;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import four.awesome.myta.message.MyMessage;
import four.awesome.myta.models.Course;
import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class CourseInfo extends AppCompatActivity {


    private String apiKey;
    private Course course;
    private String type;
    private int id;
    private boolean isJoined;

    Button appendCourseButton;
    ImageView releaseAssisgnmentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        importData();
        initialView();
        initialReleaseAssisgment();
        initialAppendCourse();
    }

    // Import data from parent activiy
    public void importData() {
        Intent intent = getIntent();
        apiKey = intent.getStringExtra("apiKey");
        type = intent.getStringExtra("type");
        id = intent.getIntExtra("userId", -1);
        isJoined = intent.getBooleanExtra("isJoined", false);
        course = new Course(intent.getIntExtra("courseId", -1),
                intent.getStringExtra("courseName"), intent.getStringExtra("teacherName"));
    }
    // Initial view.
    public void initialView() {
        ((TextView) findViewById(R.id.info_course_name)).setText(course.getName());
        ((TextView) findViewById(R.id.infor_course_teacher))
                .setText("授课人："+course.getTeacher());

        appendCourseButton = (Button) findViewById(R.id.button_append_course);
        releaseAssisgnmentButton = (ImageView) findViewById(R.id.icon_release_assisgnment);
        if (type.equals("teacher") || isJoined) {
            appendCourseButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        if (type.equals("student")) {
            releaseAssisgnmentButton.setVisibility(View.INVISIBLE);
        }
    }

    // Initial add assisgment function.
    public void initialReleaseAssisgment() {
        releaseAssisgnmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : release assisgment
            }
        });
    }
    // Initial add course function.
    public void initialAppendCourse() {
        appendCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("teacher") || isJoined) return;
                (new APIClient()).subscribeAppendCourse(new Observer<Response<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(Response<User> userResponse) {
                        if (userResponse.code() == 201) {
                            Toast.makeText(getApplicationContext(), "选课成功", Toast.LENGTH_SHORT).show();
                            appendCourseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                            EventBus.getDefault().post(new MyMessage("join", id));
                        } else if (userResponse.code() == 400) {
                            Toast.makeText(getApplicationContext(), "无法访问", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {}
                }, course.getId(), id, apiKey);
            }
        });
    }


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