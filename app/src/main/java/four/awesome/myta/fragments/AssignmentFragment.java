package four.awesome.myta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import four.awesome.myta.R;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.User;

/**
 * Fragment for homework list.
 */
public class AssignmentFragment extends Fragment {
    private Assignment assignment_data;

    // 界面组件
    private CircleImageView creator_img;
    private User assign_creator;
    private TextView assign_name;
    private TextView assign_start_time;
    private TextView assign_end_time;
    private TextView assign_detail;

    // 布局对象
    private static AssignmentFragment fragment;
    View assign_view;
    public static AssignmentFragment newInstance() {
        if (fragment == null) {
            fragment = new AssignmentFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assign_view = inflater.inflate(R.layout.fragment_assignment, container, false);

        return assign_view;
    }
    private void setView() {
        if (assign_view == null)
            System.out.println("assign fragment error!");
        creator_img = (CircleImageView) assign_view.findViewById(R.id.creator_image);
        assign_name = (TextView) assign_view.findViewById(R.id.assign_name);
        assign_start_time = (TextView) assign_view.findViewById(R.id.start_time);
        assign_end_time = (TextView) assign_view.findViewById(R.id.end_time);
        assign_detail = (TextView) assign_view.findViewById(R.id.assign_detail);
    }
    private void setData() {
//        if (assignment_data == null) {
//
//        }
    }
}
