package four.awesome.myta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import four.awesome.myta.R;
import four.awesome.myta.models.Assignment;

/**
 * Created by Rusan on 2017/5/15.
 */

public class RecyclerAdapter extends SecondaryListAdapter<RecyclerAdapter.GroupItemViewHolder, RecyclerAdapter.SubItemViewHolder> {
    private Context context;
    private List<DataTree<Date, Assignment>> dts = new ArrayList<>();
    public RecyclerAdapter(Context context) {
        this.context = context;
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
    public void addData(DataTree<Date, Assignment> dataTree) {
        for (int i = 0; i < dts.size(); i++) {
            if (dts.get(i).getGroupItem().getYear() == dataTree.getGroupItem().getYear() &&
                    dts.get(i).getGroupItem().getMonth() == dataTree.getGroupItem().getMonth()) {
                for (int j = 0; j < dataTree.getSubItems().size(); j++)
                    dts.get(i).addNew(dataTree.getSubItems().get(j));
                notifyNewData(dts);
                return;
            }
        }
        dts.add(dataTree);
        notifyNewData(dts);
    }
    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }
    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dateitem, parent, false);
        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assignitem, parent, false);
        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {
        ((GroupItemViewHolder) holder).dateText.setText(formatter.format(dts.get(groupItemIndex).getGroupItem()));
    }
    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {
        ((SubItemViewHolder) holder).assignName.setText(dts.get(groupItemIndex).getSubItems().get(subItemIndex).getName());
        ((SubItemViewHolder) holder).startTime.setText(formatter.format(dts.get(groupItemIndex).getSubItems().get(subItemIndex).getStartTime()));
        ((SubItemViewHolder) holder).endTime.setText(formatter.format(dts.get(groupItemIndex).getSubItems().get(subItemIndex).getEndTime()));
    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        Assignment assignment = dts.get(groupItemIndex).getSubItems().get(subItemIndex);
        EventBus.getDefault().post(assignment);
    }
    @Override
    public void onGroupItemLongClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {

    }
    @Override
    public void onSubItemLongClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {

    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        public GroupItemViewHolder(View itemView) {
            super(itemView);
            dateText = (TextView) itemView.findViewById(R.id.dateOfAssign);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView assignName;
        TextView startTime;
        TextView endTime;
        TextView detail;
        public SubItemViewHolder(View itemView) {
            super(itemView);
            assignName = (TextView) itemView.findViewById(R.id.assignName);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
            endTime = (TextView) itemView.findViewById(R.id.endTime);
        }
    }
}

