package four.awesome.myta.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import four.awesome.myta.MainActivity;
import four.awesome.myta.R;
import four.awesome.myta.adapter.MyCourseListAdapter;
import four.awesome.myta.adapter.RecyclerAdapter;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.Course;

/**
 * Fragment for course list.
 */
public class CourseFragment extends Fragment {

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
                newCourseDialog();
            }
        });

        importData("");

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

            }

            @Override
            public void onLongClick(int position) {

            }
        });
        courseListView.setAdapter(courseListAdapter);
        courseListView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }

    // When add new course, show dialog.
    public void newCourseDialog() {
        LayoutInflater factory = LayoutInflater.from(view.getContext());
        View contview = factory.inflate(R.layout.dialog_add_course, null);

        EditText courseName = (EditText) contview.findViewById(R.id.new_course_name);

        Button okButton = (Button) view.findViewById(R.id.button_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Create new course.


                dialog.cancel();
            }
        });

        dialog = new AlertDialog.Builder(view.getContext()).setView(contview).create();
        dialog.show();

    }

    // Return new instance of fragment.
    public static CourseFragment newInstance(String apikey) {
        if (fragment == null)
            fragment = new CourseFragment();
        fragment.importData(apikey);
        return fragment;
    }


    // Import data from back forward.
    public void importData(String apikey) {
        if (courseList == null) {
            // TODO: Import course from server.
            courseList = new ArrayList<>();
        }
        Course course = new Course(1, "离散数学", "吴向军");
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        assignment.setName("期末");
        assignments.add(assignment);
        assignments.add(assignment);
        course.setAssignmentList(assignments);

        courseList.add(course);
    }
}
