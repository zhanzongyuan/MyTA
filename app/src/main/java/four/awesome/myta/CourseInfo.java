package four.awesome.myta;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import four.awesome.myta.models.Course;

public class CourseInfo extends AppCompatActivity {


    private String apiKey;
    private Course course;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        importData();
        initialView();
    }

    // Import data from parent activiy
    public void importData() {
        Intent intent = getIntent();
        apiKey = intent.getStringExtra("apiKey");
        type = intent.getStringExtra("type");
        course = new Course(intent.getIntExtra("courseId", -1),
                intent.getStringExtra("courseName"), "未知");
    }
    // Initial view.
    public void initialView() {
        ((TextView) findViewById(R.id.info_course_name)).setText(course.getName());
        ((TextView) findViewById(R.id.infor_course_teacher))
                .setText("授课人："+course.getTeacher());
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