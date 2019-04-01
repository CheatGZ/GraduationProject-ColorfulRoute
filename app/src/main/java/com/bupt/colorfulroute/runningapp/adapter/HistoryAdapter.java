package com.bupt.colorfulroute.runningapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bupt.colorfulroute.R;

import java.util.List;

/**
 * Created by CheatGZ on 2019/2/27.
 * 万能适配器
 */

public abstract class HistoryAdapter<T> extends RecyclerView.Adapter<HistoryAdapter.VH> implements RecycleItemTouchHelper.ItemTouchHelperCallback {
    private List<T> list;

    private OnItemClickListener mOnItemClickListener;//自定义接口传值


    public HistoryAdapter(List<T> list) {
        this.list = list;

    }

    public abstract int getLayoutId(int viewType);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return VH.get(parent, getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(VH holder, final int position) {
        convert(holder, list.get(position), position);
        holder.getView(R.id.history_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    public void setOnItemClickListener(HistoryAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract void convert(VH holder, T data, int position);

    @Override
    public void onItemDelete(int positon) {
        list.remove(positon);
        notifyItemRemoved(positon);
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        //目前设置不可拖动，无效果
    }

    //自定义接口传值
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //ViewHolder
    public static class VH extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public VH(View itemView) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutId) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new VH(convertView);
        }

        public <T extends View> T getView(int id) {
            View v = mViews.get(id);
            if (v == null) {
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T) v;
        }
        public void setText(int id, String value) {
            TextView view = getView(id);
            view.setText(value);
        }
        public void setImage(int id,int position){
            TextView view=getView(id);
            if(position%2==0){
                view.setBackgroundResource(R.drawable.selector_history_btn2);
            }else {
                view.setBackgroundResource(R.drawable.selector_history_btn);
            }
        }
    }

}

