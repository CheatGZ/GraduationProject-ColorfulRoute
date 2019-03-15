package com.bupt.colorfulroute.runningapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.HistoryDetailActivity;
import com.bupt.colorfulroute.util.CheckFormat;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by CheatGZ on 2019/2/27.
 * 万能适配器
 */

public abstract class AchievementAdapter<T> extends RecyclerView.Adapter<AchievementAdapter.VH> {
    private List<T> list;

    private OnItemClickListener mOnItemClickListener;//自定义接口传值



    public AchievementAdapter(List<T> list) {

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
        //设置item监听
        holder.getView(R.id.achievement_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
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
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    /**
     * ViewHolder
     */
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

        public void setImage(int id, int icon) {
            SimpleDraweeView view = getView(id);
            Uri uri=Uri.parse("res://com.bupt.colorfulroute/"+icon);
            view.setImageURI(uri);
        }

        public void setTime(int id, long value) {
            TextView view = getView(id);
            if (value == 0) {
                view.setText("尚未解锁");
            }else {
                view.setText(CheckFormat.dateFormat2(value).substring(0, 10)+" 解锁");
            }
        }

        public void setUnShow(int icon, int title, int layout,int item, int unIcon,Boolean value) {
            SimpleDraweeView view1 = getView(icon);
            TextView view2 = getView(title);
            if (!value) {
                Uri uri=Uri.parse("res://com.bupt.colorfulroute/"+unIcon);
                view1.setImageURI(uri);
                view2.setText("未获得");

                LinearLayout view3 = getView(layout);
                LinearLayout view4=getView(item);
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(Color.parseColor("#4A4B4F"));
                gd.setCornerRadius(10f);
                view3.setBackground(gd);
                view4.setClickable(false);
            }
        }
    }

}

