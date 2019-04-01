package com.bupt.colorfulroute.runningapp.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
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
        holder.getView(R.id.achievement_show_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public abstract void convert(VH holder, T data, int position);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    //自定义接口传值
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
            Uri uri;
            switch (icon) {
                case 0:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.cat_thumbnail);
                    break;
                case 1:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.cattle_thumbnail);
                    break;
                case 2:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.fish_thumbnail);
                    break;
                case 3:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.frog_thumbnail);
                    break;
                case 4:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.monkey_thumbnail);
                    break;
                case 5:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.panda_thumbnail);
                    break;
                case 6:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.rabbit_thumbnail);
                    break;
                case 7:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.sloth_thumbnail);
                    break;
                default:
                    uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.no_achieve);
                    break;
            }
            view.setImageURI(uri);
        }

        public void setTime(int id, long value) {
            TextView view = getView(id);
            if (value == 0) {
                view.setText("尚未解锁");
            } else {
                view.setText(CheckFormat.dateFormat2(value).substring(0, 10) + " 解锁");
            }
        }

        public void setUnShow(int icon, int title, int layout, Boolean value) {
            SimpleDraweeView view1 = getView(icon);
            TextView view2 = getView(title);
            if (!value) {
                Uri uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.no_achieve);
                view1.setImageURI(uri);
                view2.setText("未获得");
                LinearLayout view3 = getView(layout);
                view3.setBackgroundResource(R.drawable.selector_no_achievement_item);
            }
        }
    }

}

