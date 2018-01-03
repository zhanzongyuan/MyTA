package four.awesome.myta.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import four.awesome.myta.R;
import four.awesome.myta.adapter.MyExpandAdapter;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.Course;

/**
 * Fragment for course list.
 */
public class CourseFragment extends Fragment {

    private static CourseFragment fragment;
    private View view;

    ExpandableListView expandableListView;
    MyExpandAdapter listAdapter;
    List<Course> courseList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Complete page logic here.
        importData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_course, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        listAdapter = new MyExpandAdapter(getContext(), courseList);
        expandableListView.setAdapter(listAdapter);

        return view;
    }

    // TODO: Complete related function here.
    public static CourseFragment newInstance() {
        if (fragment == null)
            fragment = new CourseFragment();
        return fragment;
    }

    public void importData() {
        courseList = new ArrayList<>();
        Course course = new Course("离散数学", "吴向军");
        List<Assignment> assignments = new ArrayList<>();
        Assignment assignment = new Assignment();
        assignment.setName("期末");
        assignments.add(assignment);
        assignments.add(assignment);
        course.setAssignmentList(assignments);

        courseList.add(course);
    }
}
