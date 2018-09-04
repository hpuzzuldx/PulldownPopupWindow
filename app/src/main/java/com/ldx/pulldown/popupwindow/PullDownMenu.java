package com.ldx.pulldown.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.pulldown.R;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class PullDownMenu {

    private static final int INDICATOR_TO_CONTAINER_MIN_MARGIN = 5;
    private static final int MARGIN_SCREEN = 5;
    private Context mContext;
    private PopupWindow mPopupWindow;

    private TriangleIndicatorView mTriangleUpIndicatorView;
    private TriangleIndicatorView mTriangleDownIndicatorView;
    private LinearLayout mContainerLayout;
    private PullDownLayout mDropPopLayout;

    private RecyclerView mRecycleView;
    private View mBtnView;
    private MenuItemAdapter mDropPopMenuAdapter;
    private ArrayList<MenuItem> mMenuItemList;

    private int mIndicatorToContainerMinMargin;
    private int mPopupWindowWidth = 240;

    private int mMarginScreen;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mItemHeight;
    private int mTotalHeight;
    private int mMaxTextWidth;
    private int mWidth;

    private boolean mIsShowAtUp = false;
    private PopupWindow.OnDismissListener mOnDismissListener;
    private SubmitClickListener mSubmitClickListener;
    private View contentView;
    private TextView mSelectedTips;
    private TextView mCancleView;
    private TextView mSubmitView;

    public PullDownMenu(Context context) {
        mContext = context;
        init();

        mDropPopLayout = new PullDownLayout(context);
        mTriangleUpIndicatorView = mDropPopLayout.getTriangleUpIndicatorView();
        mTriangleDownIndicatorView = mDropPopLayout.getTriangleDownIndicatorView();
        mContainerLayout = mDropPopLayout.getContainerLayout();

        mScreenWidth = getScreenWidth(mContext);
        create();
    }

    private void init() {
        mIndicatorToContainerMinMargin = dip2px(mContext, INDICATOR_TO_CONTAINER_MIN_MARGIN);
        mMarginScreen = dip2px(mContext, MARGIN_SCREEN);
        //根据实际情况计算item的高度
        mItemHeight = (int) mContext.getResources().getDimension(R.dimen.dislike_menuitem_height) + dip2px(mContext,10);

        mScreenHeight = getScreenHeight(mContext);
        mWidth = dip2px(mContext,mPopupWindowWidth);
    }


    private void create() {
        mPopupWindow = new PopupWindow(mDropPopLayout, LinearLayout.LayoutParams.MATCH_PARENT
                , LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopOnDismissListener());

        mDropPopLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mPopupWindow.dismiss();
                return true;
            }
        });
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        contentView = inflater.inflate(R.layout.layout_popupwindow, mContainerLayout,false);
        mSelectedTips = (TextView)contentView.findViewById(R.id.dislike_selected_tips);
        mCancleView = (TextView)contentView.findViewById(R.id.dislike_cancle_tv);
        mSubmitView = (TextView)contentView.findViewById(R.id.dislike_submit_tv);
        initListView();
        initEvent();
    }

    private void initEvent() {
        mCancleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });

        mSubmitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDropPopMenuAdapter != null) {
                    ArrayList<MenuItem> selectedList =  mDropPopMenuAdapter.getSelectedList();
                    if (selectedList == null){
                        mPopupWindow.dismiss();
                    }
                    if (selectedList != null && selectedList.size() <= 0){
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.dislike_please_atleast_selectone), Toast.LENGTH_SHORT).show();
                        if (mSubmitClickListener != null){
                            mSubmitClickListener.onSubmitClick(false,null);
                        }
                    }else if (selectedList != null && selectedList.size() > 0){
                        if (mSubmitClickListener != null){
                            mSubmitClickListener.onSubmitClick(true,selectedList);
                        }
                        mPopupWindow.dismiss();
                    }
                }else{
                    mPopupWindow.dismiss();
                }
            }
        });

    }

    private void initListView() {
        mRecycleView = (RecyclerView) contentView.findViewById(R.id.menu_listview);
        GridContentLayoutManager manager = new GridContentLayoutManager(mContext,2);
        mRecycleView.setLayoutManager(manager);
        mContainerLayout.addView(contentView);
    }

    public RecyclerView getRecycleView() {
        return mRecycleView;
    }

    public void setWidth(int width) {
        mWidth = width;
        if (mBtnView != null) {
            updateViewPosition(mBtnView);
        }
    }

    public void setMenuList(ArrayList<MenuItem> menuList) {
        if (mMenuItemList != null) {
            mMenuItemList.clear();
        } else {
            mMenuItemList = new ArrayList<>();
        }
        mMenuItemList.addAll(menuList);

        checkWidth();
        checkHeight();
        initView();
        if (mDropPopMenuAdapter == null) {
            mDropPopMenuAdapter = new MenuItemAdapter(mContext,mMenuItemList);
            mDropPopMenuAdapter.setClickListener(new MenuItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(int position, int size, View view) {
                    mSelectedTips.setText(String.format(mContext.getResources().getString(R.string.dislike_already_selectednum),size+""));
                }
            });
            mRecycleView.setAdapter(mDropPopMenuAdapter);
            mDropPopMenuAdapter.notifyDataSetChanged();
        } else {
            mDropPopMenuAdapter.notifyDataSetChanged();
        }

    }

    public void setBackgroundResource(int backgroundResource) {
        mDropPopLayout.setBackgroundResource(backgroundResource);
    }

    public void setBackgroundColor(int color) {
        mDropPopLayout.setBackgroundColor(color);
    }

    /**
     * arrow color
     * @param color
     */
    public void setTriangleIndicatorViewColor(int color) {
        mDropPopLayout.setTriangleIndicatorViewColor(color);
    }

    private void checkWidth() {
        mMaxTextWidth = dip2px(mContext,mPopupWindowWidth);
    }

    private void checkHeight() {
        int size = getListSize(mMenuItemList);
        //两列 + 前后bar
        mTotalHeight = ((size + 1)/2 + 2) * mItemHeight;
    }

    public void show(final View parent) {
        mBtnView = parent;
        setBackgroundAlpha(50f);
        mDropPopLayout.requestFocus();

        mIsShowAtUp = false;
        int parentHeight = parent.getHeight();
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int y = location[1];
        int popMenuHeight = mTotalHeight + mTriangleUpIndicatorView.getRealHeight();
        if (mScreenHeight - y - parentHeight < popMenuHeight) {
            mIsShowAtUp = true;
        }

        if (mIsShowAtUp) {
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, mScreenHeight - y);
        } else {
            mPopupWindow.showAsDropDown(parent, 0, 0);
        }
        updateView();
    }

    private void updateView() {
        mDropPopLayout.post(new TimerTask() {
            @Override
            public void run() {
                updateViewPosition(mBtnView);
            }
        });
    }

    private void updateViewPosition(View parent) {
        int parentWidth = parent.getMeasuredWidth();
        int[] location = new int[2];
        parent.getLocationOnScreen(location);
        int x = location[0];

        int centerX = x + parentWidth / 2;
        int leftMargin = x;
        int rightMargin = mScreenWidth - leftMargin - parentWidth;
        int containerViewHalfWidth = mWidth / 2;
        int indicatorViewHalfWidth = mTriangleUpIndicatorView.getRealWidth() / 2;

        LinearLayout.LayoutParams upIndicatorParams = (LinearLayout.LayoutParams) mTriangleUpIndicatorView.getLayoutParams();
        LinearLayout.LayoutParams containerParams = (LinearLayout.LayoutParams) mContainerLayout.getLayoutParams();
        containerParams.width = mWidth;


        if (leftMargin < rightMargin) {//在左侧
            if (leftMargin >= containerViewHalfWidth) {//显示在中间
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = centerX - containerViewHalfWidth;
            } else {
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = mMarginScreen;
                if (upIndicatorParams.rightMargin > containerParams.rightMargin - mIndicatorToContainerMinMargin
                        && mWidth <= mScreenWidth / 2) {//矫正箭头在列表右边
                    int newLeftMargin = upIndicatorParams.leftMargin - mIndicatorToContainerMinMargin;
                    if (newLeftMargin >= mMarginScreen) {
                        containerParams.leftMargin = newLeftMargin;
                    }
                }
            }
        } else if (leftMargin > rightMargin) {//在右侧
            if (rightMargin >= containerViewHalfWidth) {
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = centerX - containerViewHalfWidth;
            } else {
                upIndicatorParams.leftMargin = centerX - indicatorViewHalfWidth;
                containerParams.leftMargin = mScreenWidth - containerViewHalfWidth * 2 - mMarginScreen;
                if (upIndicatorParams.leftMargin < containerParams.leftMargin + mIndicatorToContainerMinMargin) {//矫正箭头在列表左边
                    containerParams.leftMargin = upIndicatorParams.leftMargin - mIndicatorToContainerMinMargin;
                }
            }
        } else {//在中间
            int left = centerX - indicatorViewHalfWidth;
            int right = centerX - containerViewHalfWidth;
            upIndicatorParams.leftMargin = left;
            containerParams.leftMargin = right;
        }

        if (upIndicatorParams.leftMargin <= 0) {//校正三角形指示器的边界超过范围
            upIndicatorParams.leftMargin = mMarginScreen + mIndicatorToContainerMinMargin;
        } else if (upIndicatorParams.leftMargin + indicatorViewHalfWidth * 2 >= mScreenWidth) {
            upIndicatorParams.leftMargin = mScreenWidth - indicatorViewHalfWidth * 2 - mMarginScreen - mIndicatorToContainerMinMargin;
        }

        mDropPopLayout.setOrientation(mIsShowAtUp);

        mTriangleUpIndicatorView.setLayoutParams(upIndicatorParams);
        mTriangleDownIndicatorView.setLayoutParams(upIndicatorParams);
        mContainerLayout.setLayoutParams(containerParams);
    }

    private class PopOnDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f);
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
        }
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    /***
     * screen bg alpha * @param bgAlpha
     */
    private void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int getListSize(List list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public interface SubmitClickListener{
        public void onSubmitClick(boolean haveData,ArrayList<MenuItem> menuList);
    }

    public void setSubmitClickListener(SubmitClickListener submitClickListener){
        mSubmitClickListener = submitClickListener;
    }

}
