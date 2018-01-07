package four.awesome.myta;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import four.awesome.myta.models.Assignment;

public class AssignActivity extends AppCompatActivity {
    private Assignment assignment;
    private CircleImageView createImg;
    private TextView creatorName;
    private TextView assignName;
    private TextView startTime;
    private TextView endTime;
    private TextView assignDetail;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        String type = bundle.getString("type");
        // Change theme when different user.
        if (type.equals("teacher")) {
            setTheme(R.style.AppThemeTA);
        }

        setContentView(R.layout.activity_assign);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        this.assignment = (Assignment) bundle.getSerializable("assignment");
        setView();
    }
    private void setView() {
        createImg = (CircleImageView) findViewById(R.id.creator_image);
        creatorName = (TextView) findViewById(R.id.creator_name);
        assignName = (TextView) findViewById(R.id.assign_name);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        assignDetail = (TextView) findViewById(R.id.assign_detail);
        // 用户设置头像还没写
        creatorName.setText(assignment.getCreator().getName());
        assignName.setText(assignment.getName());
        startTime.setText(formatter.format(assignment.getStartTime()));
        endTime.setText(formatter.format(assignment.getEndTime()));
        assignDetail.setText(assignment.getDetail());
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
