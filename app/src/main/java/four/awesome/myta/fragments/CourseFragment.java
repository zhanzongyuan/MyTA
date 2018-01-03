package four.awesome.myta.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import four.awesome.myta.MainActivity;
import four.awesome.myta.R;
import four.awesome.myta.adapter.MyCourseListAdapter;
import four.awesome.myta.adapter.RecyclerAdapter;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.Course;
import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Fragment for course list.
 */
public class CourseFragment extends Fragment {

    private String apiKey;
    private int id;
    private String type;

    private static CourseFragment fragment;
    private View view;
    private AlertDialog dialog;

    MyCourseListAdapter courseListAdapter;
    RecyclerView courseListView;
    List<Course> courseList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    // Initial expandable list view here.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container, false);


        // Set add new course button.
        FloatingActionButton addButton = (FloatingActionButton) view.findViewById(R.id.floatingButton_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == "teacher")
                    createCourseDialog();
                else
                    joinCourse();

            }
        });

        initialRecyclerView();

        return view;
    }

    // When teacher creates new course, show dialog.
    public void createCourseDialog() {
        LayoutInflater factory = LayoutInflater.from(view.getContext());
        View contview = factory.inflate(R.layout.dialog_add_course, null);

        final EditText courseName = (EditText) contview.findViewById(R.id.new_course_name);

        Button okButton = (Button) contview.findViewById(R.id.button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Create new course.
                createCourseHttpRequest(courseName.getText().toString());
                dialog.cancel();
            }
        });

        dialog = new AlertDialog.Builder(view.getContext()).setView(contview).create();
        dialog.show();

    }

    // When student joins course, show dialog to choose.
    public void joinCourse() {

    }

    // Return new instance of fragment.
    public static CourseFragment newInstance(String apikey, int id, String type) {
        if (fragment == null)
            fragment = new CourseFragment();

        fragment.apiKey = apikey;
        fragment.id = id;
        fragment.type = type;

        fragment.importData();

        return fragment;
    }


    // Import data from back forward.
    public void importData() {
        (new APIClient()).subscribeCourse(new Observer<Response<List<Course>>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<List<Course>> listResponse) {

                if (listResponse.code() == 200) {
                    courseList = listResponse.body();
                } else if (listResponse.code() == 400) {
                    Toast.makeText(view.getContext(), "无法访问", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {}
        }, apiKey, id);
        
    }

    public void initialRecyclerView() {
        // Initial recycler view.
        courseListView = view.findViewById(R.id.reclyerView_course);
        courseListAdapter = new MyCourseListAdapter<Course>(view.getContext(), R.layout.course_list_group, courseList);
        courseListAdapter.setOnBindDataListener(new MyCourseListAdapter.OnBindDataListener<Course>() {
            @Override
            public void onBindData(MyCourseListAdapter.ViewHolder holder, Course d) {
                TextView courseNameText = (TextView) holder.getView(R.id.textview_course_name);
                TextView teacherNameText = (TextView) holder.getView(R.id.textview_teacher_name);

                courseNameText.setText(d.getName());
                teacherNameText.setText(d.getTeacher());
            }
        });
        courseListAdapter.setOnItemClickListener(new MyCourseListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // TODO：Jump to course detail.
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        courseListView.setAdapter(courseListAdapter);
        courseListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }

    // Http request to new course.
    public void createCourseHttpRequest(final String courseName) {
        (new APIClient()).subscribeNewCourse(new Observer<Response<Course>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<Course> courseResponse) {
                System.out.println(""+courseResponse.code());
                if (courseResponse.code() == 201) {
                    Toast.makeText(view.getContext(), "成功创建", Toast.LENGTH_SHORT).show();
                } else if (courseResponse.code() == 400) {
                    Toast.makeText(view.getContext(), "无法访问", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }}

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();}

            @Override
            public void onComplete() {}
        }, apiKey, courseName, id);
    }
}
