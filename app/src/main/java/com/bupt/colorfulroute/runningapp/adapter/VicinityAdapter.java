package com.bupt.colorfulroute.runningapp.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by CheatGZ on 2019/2/27.
 * 万能适配器
 */

public abstract class VicinityAdapter<T> extends RecyclerView.Adapter<VicinityAdapter.VH> {
    private List<T> list;

    private OnItemClickListener mOnItemClickListener;//自定义接口传值


    public VicinityAdapter(List<T> list) {
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
//        holder.getView(R.id.vicinity_content).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
//            }
//        });
    }

    public void setOnItemClickListener(VicinityAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract void convert(VH holder, T data, int position);

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
        public void setIcon(int id,String value){
            SimpleDraweeView view=getView(id);
            view.setImageURI(value);
        }
    }

}

