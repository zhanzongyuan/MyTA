package four.awesome.myta.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by applecz on 2018/1/3.
 */


public class MyCourseListAdapter<T> extends RecyclerView.Adapter<MyCourseListAdapter.ViewHolder> {
    private List<T> mDataset;
    private Context mContext;
    private int mLayoutId;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyCourseListAdapter(Context context, int id, List<T> myDataset) {
        mContext=context;
        mLayoutId=id;
        mDataset = myDataset;
    }


    @Override
    public MyCourseListAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                   int viewType) {

        View itemView= LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        MyCourseListAdapter.ViewHolder viewHolder=new MyCourseListAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyCourseListAdapter.ViewHolder holder, int position) {
        mOnBindDataListener.onBindData(holder, mDataset.get(position));
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private MyCourseListAdapter.OnBindDataListener mOnBindDataListener;
    public interface OnBindDataListener<T> {
        void onBindData(MyCourseListAdapter.ViewHolder holder, T d);
    }


    public void setOnBindDataListener(MyCourseListAdapter.OnBindDataListener onBindDataListener) {
        mOnBindDataListener = onBindDataListener;
    }

    private MyCourseListAdapter.OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(MyCourseListAdapter.OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;
        public ViewHolder(View v) {
            super(v);
            mConvertView=v;
            mViews=new SparseArray<View>();
        }
        public View getView(int viewId){
            View view=mViews.get(viewId);
            if (view==null){
                view=mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
    }

}
