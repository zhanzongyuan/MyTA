package four.awesome.myta.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import four.awesome.myta.models.Assignment;

/**
 * Description: Providing a adapter to implement secondary list base on
 * {@link RecyclerView.Adapter} for {@link RecyclerView}.
 * <!--
 * Author: Rusan
 * Date: 2017/4/20
 * Version: 0.0.1
 * ---------------------------------------------
 * History:
 * <p>
 * -->
 */

public abstract class SecondaryListAdapter<GVH, SVH extends RecyclerView.ViewHolder> extends RecyclerView
        .Adapter<RecyclerView.ViewHolder> {
    private List<Boolean> groupItemStatus = new ArrayList<>();
    private List<DataTree> dataTrees = new ArrayList<>();

    /**
     * Set new data for adapter to show. It must be called when set new data.
     *
     * @param data New data
     *
     */
    public void notifyNewData(List data) {
        Collections.sort(data, new Comparator<DataTree<Date, Assignment>>() {
            @Override
            public int compare(DataTree<Date, Assignment> o1, DataTree<Date, Assignment> o2) {
                return ((Date) o1.getGroupItem()).compareTo(o2.getGroupItem());
            }
        });
        setDataTrees(data);
    }
    // 向外暴露设置显示数据的方法
    private final void setDataTrees(List dt) {
        this.dataTrees = dt;
        initGroupItemStatus(groupItemStatus);
        notifyDataSetChanged();
    }

    //设置初始值，所有 groupItem 默认为关闭状态
    private void initGroupItemStatus(List l) {
        for (int i = 0; i < dataTrees.size(); i++) {
            l.add(false);
        }
    }


    /**
     * Create group item view holder for onCreateViewHolder.
     *
     * @param parent Provided by onCreateViewHolder.
     *
     */
    public abstract RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent);

    /**
     * Create subitem view holder for onCreateViewHolder.
     *
     * @param parent Provided by onCreateViewHolder.
     *
     */
    public abstract RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent);


    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == ItemStatus.VIEW_TYPE_GROUPITEM) {
            viewHolder = groupItemViewHolder(parent);
        } else if (viewType == ItemStatus.VIEW_TYPE_SUBITEM) {
            viewHolder = subItemViewHolder(parent);
        }
        return viewHolder;
    }

    /**
     * Update the content of specified group item. The method will called by onBindViewHolder.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     *
     * @param groupItemIndex The index of the group item.
     *
     */
    public abstract void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int
            groupItemIndex);

    /**
     * Update the content of specified subitem. The method will called by onBindViewHolder.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param subItemIndex The index of the subitem.
     *
     */
    public abstract void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int
            groupItemIndex, int subItemIndex);
    // 定义点击事件，抽象函数
    public abstract void onGroupItemClick(Boolean isExpand, GVH holder, int groupItemIndex);
    public abstract void onSubItemClick(SVH holder, int groupItemIndex, int subItemIndex);
    public abstract void onGroupItemLongClick(Boolean isExpand, GVH holder, int groupItemIndex);
    public abstract void onSubItemLongClick(SVH holder, int groupItemIndex, int subItemIndex);

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ItemStatus itemStatus = getItemStatusByPosition(position);
        final DataTree dt = dataTrees.get(itemStatus.getGroupItemIndex());
        if ( itemStatus.getViewType() == ItemStatus.VIEW_TYPE_GROUPITEM ) {
            onGroupItemBindViewHolder(holder, itemStatus.getGroupItemIndex());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int groupItemIndex = itemStatus.getGroupItemIndex();
                    if ( !groupItemStatus.get(groupItemIndex) ) {
                        onGroupItemClick(false, (GVH) holder, groupItemIndex);
                        groupItemStatus.set(groupItemIndex, true);
                        notifyItemRangeInserted(holder.getAdapterPosition() + 1, dt.getSubItems
                                ().size());
                    } else {
                        onGroupItemClick(true, (GVH) holder, groupItemIndex);
                        groupItemStatus.set(groupItemIndex, false);
                        notifyItemRangeRemoved(holder.getAdapterPosition() + 1, dt.getSubItems
                                ().size());
                    }
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int groupItemIndex = itemStatus.getGroupItemIndex();
                    dataTrees.remove(groupItemIndex);
                    notifyDataSetChanged();
                    return true;
                }
            });
        } else if (itemStatus.getViewType() == ItemStatus.VIEW_TYPE_SUBITEM) {
            onSubItemBindViewHolder(holder, itemStatus.getGroupItemIndex(), itemStatus
                    .getSubItemIndex());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSubItemClick((SVH) holder, itemStatus.getGroupItemIndex(), itemStatus.getSubItemIndex());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dataTrees.get(itemStatus.getGroupItemIndex()).removeChildByPosition(itemStatus.getSubItemIndex());
                    notifyDataSetChanged();
                    return true;
                }
            });
        }
    }
    @Override
    public final int getItemCount() {
        int itemCount = 0;
        if (groupItemStatus.size() == 0) {
            return 0;
        }
        for (int i = 0; i < dataTrees.size(); i++) {
            if (groupItemStatus.get(i)) {
                itemCount += dataTrees.get(i).getSubItems().size() + 1;
            } else {
                itemCount++;
            }
        }
        return itemCount;
    }
    @Override
    public final int getItemViewType(int position) {
        return getItemStatusByPosition(position).getViewType();
    }
    private ItemStatus getItemStatusByPosition(int position) {
        ItemStatus itemStatus = new ItemStatus();
        int count = 0;
        int i = 0;
        for (i = 0; i < groupItemStatus.size(); i++ ) {
            if (count == position) {
                itemStatus.setViewType(ItemStatus.VIEW_TYPE_GROUPITEM);
                itemStatus.setGroupItemIndex(i);
                break;
            } else if (count > position) {
                itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUBITEM);
                itemStatus.setGroupItemIndex(i - 1);
                itemStatus.setSubItemIndex(position - ( count - dataTrees.get(i - 1).getSubItems
                        ().size() ) );
                break;
            }
            count++;
            if (groupItemStatus.get(i)) {
                count += dataTrees.get(i).getSubItems().size();
            }
        }
        if (i >= groupItemStatus.size()) {
            itemStatus.setGroupItemIndex(i - 1);
            itemStatus.setViewType(ItemStatus.VIEW_TYPE_SUBITEM);
            itemStatus.setSubItemIndex(position - ( count - dataTrees.get(i - 1).getSubItems().size
                    () ) );
        }
        return itemStatus;
    }


    // 封装每一项的状态
    private static class ItemStatus {
        public static final int VIEW_TYPE_GROUPITEM = 0;
        public static final int VIEW_TYPE_SUBITEM = 1;
        private int viewType;
        private int groupItemIndex = 0;
        private int subItemIndex = -1;
        public ItemStatus() {
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }

        public int getGroupItemIndex() {
            return groupItemIndex;
        }

        public void setGroupItemIndex(int groupItemIndex) {
            this.groupItemIndex = groupItemIndex;
        }

        public int getSubItemIndex() {
            return subItemIndex;
        }

        public void setSubItemIndex(int subItemIndex) {
            this.subItemIndex = subItemIndex;
        }
    }


    /**
     * Created by Rusan on 2017/4/12.
     */

    public final static class DataTree<Date, Assignment> {
        private Date groupItem;
        private List<Assignment> subItems;
        public DataTree(Date groupItem, List<Assignment> subItems) {
            this.groupItem = groupItem;
            this.subItems = subItems;
        }
        public void removeChildByPosition(int position) {
            subItems.remove(position);
        }
        public void addNew(Assignment v) {
            subItems.add(v);
        }
        public Date getGroupItem() {
            return groupItem;
        }

        public List<Assignment> getSubItems() {
            return subItems;
        }
    }
}
