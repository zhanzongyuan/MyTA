package four.awesome.myta.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import four.awesome.myta.R;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.Course;

/**
 * Created by applecz on 2018/1/3.
 */

public class MyExpandAdapter extends BaseExpandableListAdapter {


    List<Course> group_head;
    List<List<Assignment>> child;
    Context context;

    public MyExpandAdapter(Context content){
        // Initial ExpandAdapter.
        group_head = new ArrayList<Course>();
        child = new ArrayList<List<Assignment>>();
    }

    public MyExpandAdapter(Context context, List<Course> group_head){
        this.context = context;
        // Initial ExpandAdapter with data in group_head.
        this.group_head = group_head;
        this.child = new ArrayList<List<Assignment>>();
        for (int i = 0; i < group_head.size(); i++) {
            this.child.add(group_head.get(i).getAssignmentList());
        }
    }


    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return this.group_head.size();
    }

    @Override
    public int getChildrenCount(int position) {
        // TODO Auto-generated method stub
        if(position<0||position>=this.child.size())
            return 0;
        return child.get(position).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return group_head.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return child.get(childPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // Make layout of group element.
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.course_list_group, null);
        }
        TextView nameTextView = convertView.findViewById(R.id.textview_course_name);
        nameTextView.setText(group_head.get(groupPosition).getName());
        TextView teacherTextView = convertView.findViewById(R.id.textview_teacher_name);
        teacherTextView.setText(group_head.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // Make layout of child elment.
        if(convertView ==null){
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.course_list_item, null);
        }
        TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_assignment_name);
        String name = child.get(groupPosition).get(childPosition).getName();
        nameTextView.setText(name);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        //调用Activity里的ChildSelect方法
        //context.childSelect(groupPosition,childPosition);
        return true;
    }
}
