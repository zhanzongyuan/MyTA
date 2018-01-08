package four.awesome.myta.fragments;


import android.content.Context;
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
    private Context context;

    public static AssignmentListFragment newInstance(Context context) {
        // 这里这么设置是为了解除注销重新登陆新的用户的时候出现的assign列表没刷新的bug，其实应该处理的是activity之间的跳转
        fragment = new AssignmentListFragment();
        fragment.setContext(context);
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
        recyclerAdapter = new RecyclerAdapter(this.context);
        recyclerAdapter.setData(datas);
        assign_list_recyclerView.setAdapter(recyclerAdapter);
        return assign_list_view;
    }
    public void setType(String type) {
        if (type.equals("teacher"))
            recyclerAdapter.setUserType(true);
        else
            recyclerAdapter.setUserType(false);
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
    // 重新设置某一项assignment
    public void setAssignmentByPositon() {

    }
    // 添加assignment
    public void addAssignment(final Assignment addAssign) {
        //datas.add(new SecondaryListAdapter.DataTree<Date, Assignment>(assignment.getEndTime(), new ArrayList<Assignment>(){{add(assignment);}}));
        recyclerAdapter.addData(new SecondaryListAdapter.DataTree<Date, Assignment>(addAssign.getEndTime(), new ArrayList<Assignment>(){{add(addAssign);}}));
        recyclerAdapter.notifyDataSetChanged();
    }
    // 添加所有assignment
    public void addAllAssignment(final List<Assignment> list, String userType) {
        recyclerAdapter.removeAll();
        for (int i = 0; i < list.size(); i++) {
            final int pos = i;
            recyclerAdapter.addData(new SecondaryListAdapter.DataTree<Date, Assignment>(list.get(pos).getEndTime(), new ArrayList<Assignment>(){{add(list.get(pos));}}));
            recyclerAdapter.notifyDataSetChanged();
        }
        setType(userType);
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public void setApiKey(String apiKey) {
        recyclerAdapter.setApiKey(apiKey);
    }
}
