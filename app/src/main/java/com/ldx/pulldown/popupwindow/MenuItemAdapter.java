package com.ldx.pulldown.popupwindow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldx.pulldown.R;

import java.util.ArrayList;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.NewsItemHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mClickListener;
    private OnChildClickListener mChildClickListener;
    private ArrayList<MenuItem> mDataList;
    private ArrayList<MenuItem> mSelectedList;
    private ArrayList<Integer> mSelectedPos;

    public interface OnItemClickListener {
        void onItemClicked(int position, int size,View view);
    }

    public interface OnChildClickListener {
        void onChildClicked(int position, View view);
    }

    public void setClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void setChildClickListener(OnChildClickListener mChildClickListener) {
        this.mChildClickListener = mChildClickListener;
    }

    public MenuItemAdapter(Context context, ArrayList<MenuItem> dataList) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDataList = dataList;
        mSelectedList = new ArrayList<MenuItem>();
        mSelectedPos = new ArrayList<Integer>();
    }

    @Override
    public NewsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NewsItemHolder viewHolder =  new TitleItemHolder(mInflater.inflate(R.layout.dislike_popitem_view, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsItemHolder holder, int position) {
        holder.setNewsItem(mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public abstract class NewsItemHolder extends RecyclerView.ViewHolder {

        public NewsItemHolder(View itemView) {
            super(itemView);
        }

        public abstract void setNewsItem(MenuItem item, int position);
    }

    private class TitleItemHolder extends NewsItemHolder {
        LinearLayout textTvContainer;
        TextView textTv;

        public TitleItemHolder(View itemView) {
            super(itemView);
            textTvContainer = (LinearLayout) itemView.findViewById(R.id.tv_text_container);
            textTv = (TextView) itemView.findViewById(R.id.tv_text);
        }

        @Override
        public void setNewsItem(final MenuItem item, final int position) {
            if(item != null) {
                textTv.setText(item.getItemTitle());
                if (mSelectedList.contains(item)){
                    textTv.setTextColor(mContext.getResources().getColor(R.color.dislike_selected_itemcolor));
                    textTvContainer.setBackgroundResource(R.drawable.dislike_selected_itembg);
                }else{
                    textTv.setTextColor(mContext.getResources().getColor(android.R.color.black));
                    textTvContainer.setBackgroundResource(R.drawable.dislike_unselected_itembg);
                }

            }

            textTvContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if (mSelectedList.contains(item)){
                      mSelectedList.remove(item);
                  }else{
                      mSelectedList.add(item);
                  }
                    notifyItemChanged(position);
                  if (mClickListener != null){
                      mClickListener.onItemClicked(position,mSelectedList == null ?0:mSelectedList.size(),v);
                  }
                }
            });

        }
    }

    public ArrayList<MenuItem> getSelectedList(){
        return mSelectedList;
    }

}
