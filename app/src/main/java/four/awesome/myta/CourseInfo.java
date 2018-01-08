package four.awesome.myta;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import four.awesome.myta.adapter.MyCourseListAdapter;
import four.awesome.myta.message.MyMessage;
import four.awesome.myta.models.Assignment;
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
    Button attendance_button;
    ImageView releaseAssisgnmentButton;
    private User teacher;
    private List<Assignment> assignments;
    private MyCourseListAdapter myAsgmListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        importData();
        setContentView(R.layout.activity_course_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialView();
        initialReleaseAssisgment();
        initialAppendCourse();
        initialAttendanceButton();
        initialClassmate();
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

        // Change theme when different user.
        if (type.equals("teacher")) {
            setTheme(R.style.AppThemeTA);
        }
        getTeacher();
    }
    // Initial view.
    public void initialView() {
        ((TextView) findViewById(R.id.info_course_name)).setText(course.getName());
        ((TextView) findViewById(R.id.infor_course_teacher))
                .setText("授课人："+course.getTeacher());
        appendCourseButton = (Button) findViewById(R.id.button_append_course);
        attendance_button = (Button) findViewById(R.id.attendance_button);
        releaseAssisgnmentButton = (ImageView) findViewById(R.id.icon_release_assisgnment);
        if (type.equals("teacher") || isJoined) {
            appendCourseButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }
        if (type.equals("student")) {
            releaseAssisgnmentButton.setVisibility(View.INVISIBLE);
        }
        initialAsgmView();
    }

    private AlertDialog alertDialog;
    // Initial add assisgment function.
    public void initialReleaseAssisgment() {
        releaseAssisgnmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : release assisgment
                showReleaseDialog();
            }
        });
    }
    public void showReleaseDialog() {
        View contview = initialDialogView();
        final EditText asgmName = (EditText) contview.findViewById(R.id.editText_assisgnment_name);
        final EditText asgmDDLDate = (EditText) contview.findViewById(R.id.editText_ddl_date);
        final EditText asgmDDLTime = (EditText) contview.findViewById(R.id.editText_ddl_time);
        final EditText asgmDetail = (EditText) contview.findViewById(R.id.editText_assisgnment_detail);
        Button okButton = (Button) contview.findViewById(R.id.button_release_assisgn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (asgmName.getText().toString().length() == 0
                        || asgmDDLDate.getText().toString().length() == 0
                        || asgmDetail.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "信息不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // TODO: http release assisgnment.
                String ddlEndStr = asgmDDLDate.getText().toString() + " " + asgmDDLTime.getText().toString();
                createAsgmHttpRequest(asgmName.getText().toString(), ddlEndStr, asgmDetail.getText().toString());
                alertDialog.cancel();
            }
        });
        alertDialog = new AlertDialog.Builder(this).setView(contview).create();
        alertDialog.show();
    }
    private View initialDialogView() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View contview = factory.inflate(R.layout.dialog_release_assisgnment, null);

        // ddl date
        final EditText ddlDatePicker = (EditText) contview.findViewById(R.id.editText_ddl_date);
        ddlDatePicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePicker(contview.getContext(), ddlDatePicker);
                    return true;
                }
                return false;
            }
        });
        ddlDatePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(contview.getContext(), ddlDatePicker);
                }
            }
        });

        // ddl time
        final EditText ddlTimePicker = (EditText) contview.findViewById(R.id.editText_ddl_time);
        ddlTimePicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showTimePicker(contview.getContext(), ddlTimePicker);
                    return true;
                }
                return false;
            }
        });
        ddlTimePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker(contview.getContext(), ddlTimePicker);
                }
            }
        });
        return contview;
    }
    private void showDatePicker(Context context, final EditText ddlDatePicker) {
        // Get the date of today.
        Calendar calendar = Calendar.getInstance();
        // Show datePicker dialog.
        final DatePickerDialog datePickerDialog =
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        String monStr = ((monthOfYear < 10) ? ("0" + monthOfYear) : ("" + monthOfYear));
                        String dayStr = ((dayOfMonth < 10) ? ("0" + dayOfMonth) : ("" + dayOfMonth));
                        ddlDatePicker.setText(year + "-" + monStr + "-" + dayStr);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void showTimePicker(Context context, final EditText ddlTimePicker) {
        // Get the date of today.
        Calendar calendar = Calendar.getInstance();
        // Show datePicker dialog.
        final TimePickerDialog timePickerDialog =
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourStr = ((hourOfDay < 10) ? "0"  : "") + hourOfDay;
                        String minStr = ((minute < 10) ? "0" : "") + minute;
                        ddlTimePicker.setText(hourStr + ":" + minStr);
                    }
                }, calendar.HOUR_OF_DAY, calendar.MINUTE, true);
        timePickerDialog.show();
    }

    // Get date and time string for now.
    private String getNowStr() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String monStr = ((month < 10) ? "0"  : "") + month;
        String dayStr = ((day < 10) ? "0" : "") + day;
        String hourStr = ((hour < 10) ? "0"  : "") + hour;
        String minStr = ((min < 10) ? "0" : "") + min;
        return year + "-" + monStr + "-" + dayStr + " " + hourStr + ":" + minStr;
    }

    // Create assisgnment http request.
    private void createAsgmHttpRequest(String assName, String eTime, String detail) {
        String pTime = getNowStr();
        int courseId = course.getId();
        String courseName = course.getName();
        (new APIClient()).subscribeCreateAssign(new Observer<Response<Assignment>>() {
            @Override
            public void onSubscribe(Disposable d) {}
            @Override
            public void onNext(Response<Assignment> assignmentResponse) {
                System.out.println(assignmentResponse.code());
                if (assignmentResponse.code() == 201) {
                    isJoined = true;
                    Toast.makeText(getApplicationContext(), "创建成功", Toast.LENGTH_SHORT).show();
                    assignmentResponse.body().setCreator(teacher);
                    assignments.add(assignmentResponse.body());
                    myAsgmListAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(assignmentResponse.body());
                }
                else if (assignmentResponse.code() == 400) {
                    Toast.makeText(getApplicationContext(), "创建失败", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {e.printStackTrace();}

            @Override
            public void onComplete() {}
        }, apiKey, assName, pTime, eTime, detail, courseId, courseName);
        System.out.println(apiKey);
        System.out.println(assName);
        System.out.println(pTime);
        System.out.println(eTime);
        System.out.println(detail);
        System.out.println(courseId);
        System.out.println(courseName);
    }

    public void getTeacher() {
        (new APIClient()).subscribeGetUsers(new Observer<Response<List<User>>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<List<User>> listResponse) {
                if (listResponse.code() == 200) {
                    Log.d("success", "在courseinfo界面获取teacher成功");
                    teacher = listResponse.body().get(0);
                } else if (listResponse.code() == 400) {
                    Log.d("error", "在courseinfo界面获取teacher网络错误");
                } else {
                    Log.d("error", "在courseinfo界面获取teacher其他错误");
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d("error", "在courseinfo界面获取teacher其他错误");
            }
            @Override
            public void onComplete() {}
        }, apiKey, course.getId(), "teacher");
    }

    // Initial add course function.
    public void initialAppendCourse() {
        appendCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("teacher") || isJoined) return;
                isJoined = true;
                (new APIClient()).subscribeAppendCourse(new Observer<Response<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onNext(Response<User> userResponse) {
                        if (userResponse.code() == 201) {
                            Toast.makeText(getApplicationContext(), "选课成功", Toast.LENGTH_SHORT).show();
                            appendCourseButton.setBackgroundColor(getResources().getColor(R.color.gray));
                            // Post it is add.
                            EventBus.getDefault().post(new MyMessage("join", course.getId()));
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

    // Initial assisgment list view.
    public void initialAsgmView() {
        // Request data.
        assignments = new ArrayList<>();
        myAsgmListAdapter = new MyCourseListAdapter(this,
                R.layout.normal_list_item, assignments);
        (new APIClient()).subscribeGetAllAssign(new Observer<Response<List<Assignment>>>() {
            @Override
            public void onSubscribe(Disposable d) {}
            @Override
            public void onNext(Response<List<Assignment>> listResponse) {
                if (listResponse.code() == 200) {
                    for (int i = 0; i < listResponse.body().size(); i++)
                        if (listResponse.body().get(i).getCourseId() == course.getId())
                            assignments.add(listResponse.body().get(i));
                    myAsgmListAdapter.notifyDataSetChanged();
                } else if (listResponse.code() == 400) {
                    Toast.makeText(getApplicationContext(), "无法访问", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {e.printStackTrace();}

            @Override
            public void onComplete() {}
        }, apiKey);
        // Initial view.
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview_course_asgm);
        myAsgmListAdapter.setOnBindDataListener(new MyCourseListAdapter.OnBindDataListener<Assignment>() {
            @Override
            public void onBindData(MyCourseListAdapter.ViewHolder holder, Assignment d) {
                ((TextView) holder.getView(R.id.name)).setText(d.getName());
            }
        });
        myAsgmListAdapter.setOnItemClickListener(new MyCourseListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(CourseInfo.this, AssignActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putSerializable("assignment", assignments.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {}
        });
        rv.setAdapter(myAsgmListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    // Initial classmate view.
    public void initialClassmate() {
        final List<User> usersList = new ArrayList<>();
        final MyCourseListAdapter myUsersListAdapter = new MyCourseListAdapter(this,
                R.layout.normal_list_item, usersList);
        (new APIClient()).subscribeGetUsers(new Observer<Response<List<User>>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<List<User>> listResponse) {
                if (listResponse.code() == 200) {
                    for (int i = 0; i < listResponse.body().size(); i++) {
                        usersList.add(listResponse.body().get(i));
                    }
                    myUsersListAdapter.notifyDataSetChanged();
                } else if (listResponse.code() == 400) {
                    Toast.makeText(getApplicationContext(), "无法访问", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {e.printStackTrace();}

            @Override
            public void onComplete() {}
        }, apiKey, course.getId(), "student");

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview_classmate);
        myUsersListAdapter.setOnBindDataListener(new MyCourseListAdapter.OnBindDataListener<User>() {
            @Override
            public void onBindData(MyCourseListAdapter.ViewHolder holder, User d) {
                ((TextView) holder.getView(R.id.name)).setText(d.getName());
            }
        });
        rv.setAdapter(myUsersListAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
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

    //initial attendance button.
    private void initialAttendanceButton() {
        final Context context = this;
        attendance_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AttendanceCheck.class);
                System.out.println("apiKey = " + apiKey);
                intent.putExtra("apiKey", apiKey);
                intent.putExtra("courseId", course.getId());
                intent.putExtra("userId", id);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }
}