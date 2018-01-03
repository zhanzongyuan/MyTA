package four.awesome.myta.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import four.awesome.myta.R;
import four.awesome.myta.adapter.RecyclerAdapter;
import four.awesome.myta.adapter.RvDividerItemDecoration;
import four.awesome.myta.adapter.SecondaryListAdapter;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentListFragment extends Fragment {
    // 布局对象
    private static AssignmentListFragment fragment;
    View assign_list_view;
    // 界面组件
    private RecyclerView assign_list_recyclerView;
    // 数据
    private List<SecondaryListAdapter.DataTree<Date, Assignment>> datas = new ArrayList<>();
    private RecyclerAdapter recyclerAdapter;

    public static AssignmentListFragment newInstance() {
        if (fragment == null) {
            fragment = new AssignmentListFragment();
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
        // Inflate the layout for this fragment
        assign_list_view = inflater.inflate(R.layout.fragment_assignment_list, container, false);
        setView();
        setData();
        recyclerAdapter = new RecyclerAdapter(getContext());
        recyclerAdapter.setData(datas);
        assign_list_recyclerView.setAdapter(recyclerAdapter);
        EventBus.getDefault().register(this);
        return assign_list_view;
    }
    private void setView() {
        if (assign_list_view == null) {
            Log.d("Error","assign fragment error!");
        }
        assign_list_recyclerView = assign_list_view.findViewById(R.id.assign_list);
        assign_list_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assign_list_recyclerView.setHasFixedSize(true);
        assign_list_recyclerView.addItemDecoration(new RvDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }
    private void setData() {
//        User temp_user = new User(
//                "张涵玮",
//                "", "张涵玮", "17665310114","student", "123@qq.com");

        User temp_user = new User();
        final Assignment assignment1 = new Assignment("assign1",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "细节",temp_user.getUsername());
        final Assignment assignment2 = new Assignment("assign2",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "细节", temp_user.getUsername());
        final Assignment assignment3 = new Assignment("assign3",
                new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                "细节", temp_user.getUsername());
        ArrayList<Assignment> tempList = new ArrayList<>();
        tempList.add(assignment1);
        tempList.add(assignment2);
        tempList.add(assignment3);
        datas.add(new SecondaryListAdapter.DataTree<Date, Assignment>(new Date(System.currentTimeMillis()), tempList));
    }
    private void changeFragment(Assignment assignment) {
        AssignmentFragment assignmentFragment = new AssignmentFragment();
        assignmentFragment.setAssignment_data(assignment);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.replace(R.id.frame_layout, assignmentFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Subscribe
    public void onEventMainThread(Assignment assignment) {
        Log.d("Eventbus", assignment.getName());
        changeFragment(assignment);
    }

}
