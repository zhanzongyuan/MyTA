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

import four.awesome.myta.CourseInfo;
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
    MyCourseListAdapter allCourseListAdapter;
    RecyclerView courseListView;
    RecyclerView allCourseListView;
    List<Course> courseList;
    List<Course> allCourseList;
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
                if (type.equals("teacher"))
                    createCourseDialog();
                else if (type.equals("student"))
                    joinCourseSwitch();

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
                if (courseName.getText().toString().length() == 0) {
                    Toast.makeText(view.getContext(), "不能为空", Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
                createCourseHttpRequest(courseName.getText().toString());
                dialog.cancel();
            }
        });

        dialog = new AlertDialog.Builder(view.getContext()).setView(contview).create();
        dialog.show();

    }

    // When student joins course, show dialog to choose.
    public void joinCourseSwitch() {
        // Get all course data for student.

        if (courseListView.getVisibility() == View.VISIBLE) {
            loadAllCourse();
            allCourseListAdapter.notifyDataSetChanged();

            courseListView.setVisibility(View.INVISIBLE);
            allCourseListView.setVisibility(View.VISIBLE);
            ((FloatingActionButton) view.findViewById(R.id.floatingButton_add))
                    .setImageResource(R.drawable.ic_arrow_back_black_24dp);
            ((FloatingActionButton) view.findViewById(R.id.floatingButton_add))
                    .setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        else {
            courseListView.setVisibility(View.VISIBLE);
            allCourseListView.setVisibility(View.INVISIBLE);
            ((FloatingActionButton) view.findViewById(R.id.floatingButton_add))
                    .setImageResource(R.drawable.ic_add_black_24dp);
            ((FloatingActionButton) view.findViewById(R.id.floatingButton_add))
                    .setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }


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
        courseList = new ArrayList<>();
        allCourseList = new ArrayList<>();
        APIClient apiClient = new APIClient();

        // Add course list of the user.
        apiClient.subscribeCourse(new Observer<Response<List<Course>>>() {
            @Override
            public void onSubscribe(Disposable d) {}

            @Override
            public void onNext(Response<List<Course>> listResponse) {

                if (listResponse.code() == 200) {
                    courseList.clear();
                    if (listResponse.body() != null)
                        courseList.addAll(listResponse.body());
                    courseListAdapter.notifyDataSetChanged();
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

    public void loadAllCourse() {
        if (type.equals("student")) {
            // Load all course
            (new APIClient()).subscribeAllCourse(new Observer<Response<List<Course>>>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onNext(Response<List<Course>> listResponse) {
                    if (listResponse.code() == 200) {
                        allCourseList.clear();
                        if (listResponse.body() != null)
                            allCourseList.addAll(listResponse.body());
                        System.out.println(allCourseList.toString());
                        allCourseListAdapter.notifyDataSetChanged();
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
                public void onComplete() {
                }
            }, apiKey);
        }


    }

    // Initial recyclerView.
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
                Intent intent = new Intent(view.getContext(), CourseInfo.class);
                intent.putExtra("apiKey", apiKey);
                intent.putExtra("type", type);
                intent.putExtra("courseId", courseList.get(position).getId());
                intent.putExtra("courseName", courseList.get(position).getName());

                startActivityForResult(intent, 0);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        courseListView.setAdapter(courseListAdapter);
        courseListView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        // Initial recycler view of all course.
        if (type.equals("student")) {
            allCourseListView = (RecyclerView) view.findViewById(R.id.reclyerView_all_course);
            allCourseListAdapter = new MyCourseListAdapter<Course>(view.getContext(), R.layout.all_course_list_group, allCourseList);
            allCourseListAdapter.setOnBindDataListener(new MyCourseListAdapter.OnBindDataListener<Course>() {
                @Override
                public void onBindData(MyCourseListAdapter.ViewHolder holder, Course d) {
                    TextView courseNameText = (TextView) holder.getView(R.id.textview_course_name);
                    TextView teacherNameText = (TextView) holder.getView(R.id.textview_teacher_name);

                    courseNameText.setText(d.getName());
                    teacherNameText.setText(d.getTeacher());

                    // IsSelected has different text color.
                    if (isCourseSelected(d)) {
                        courseNameText.setTextColor(getResources().getColor(R.color.black));
                    }
                }
            });
            allCourseListAdapter.setOnItemClickListener(new MyCourseListAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    // TODO: Jump to detail.
                    Intent intent = new Intent(view.getContext(), CourseInfo.class);
                    intent.putExtra("apiKey", apiKey);
                    intent.putExtra("type", type);
                    intent.putExtra("courseId", allCourseList.get(position).getId());
                    intent.putExtra("courseName", allCourseList.get(position).getName());

                    startActivityForResult(intent, 0);
                }

                @Override
                public void onLongClick(int position) {
                }
            });
            allCourseListView.setAdapter(allCourseListAdapter);
            allCourseListView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }

    // Judge whether course selected.
    public boolean isCourseSelected(Course d) {
        for (int i = 0; i < courseList.size(); i++) {
            if (d.getId() == courseList.get(i).getId()) return true;
        }
        return false;
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
                    courseList.add(courseResponse.body());
                    courseListAdapter.notifyDataSetChanged();
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

    // Receive result from course info page.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
