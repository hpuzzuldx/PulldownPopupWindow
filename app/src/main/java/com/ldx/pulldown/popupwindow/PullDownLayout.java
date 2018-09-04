package com.ldx.pulldown.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.ldx.pulldown.R;

public class PullDownLayout extends LinearLayout {

    private TriangleIndicatorView mTriangleUpIndicatorView;
    private TriangleIndicatorView mTriangleDownIndicatorView;
    private LinearLayout mContainerLayout;

    private int mBackgroundResource = R.drawable.bg_pulldown_shap;

    public PullDownLayout(Context context) {
        super(context);
        initView();
    }

    public PullDownLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(VERTICAL);
        setGravity(Gravity.LEFT);

        mTriangleUpIndicatorView = new TriangleIndicatorView(getContext());
        mTriangleUpIndicatorView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(mTriangleUpIndicatorView);

        mContainerLayout = new LinearLayout(getContext());
        mContainerLayout.setOrientation(VERTICAL);
        mContainerLayout.setBackgroundResource(mBackgroundResource);
        mContainerLayout.setGravity(Gravity.CENTER);

        addView(mContainerLayout, LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mTriangleDownIndicatorView = new TriangleIndicatorView(getContext());
        mTriangleDownIndicatorView.setOrientation(false);
        mTriangleUpIndicatorView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT
                , LinearLayout.LayoutParams.WRAP_CONTENT));
        addView(mTriangleDownIndicatorView);
        mTriangleDownIndicatorView.setVisibility(GONE);
    }

    /**
     * contentview location
     * @param isUp true = up
     */
    public void setOrientation(boolean isUp) {
        if (isUp) {
            mTriangleUpIndicatorView.setVisibility(GONE);
            mTriangleDownIndicatorView.setVisibility(VISIBLE);
        } else {
            mTriangleUpIndicatorView.setVisibility(VISIBLE);
            mTriangleDownIndicatorView.setVisibility(GONE);
        }
    }

    public void setBackgroundResource(int backgroundResource) {
        mBackgroundResource = backgroundResource;
        if (mContainerLayout != null) {
            mContainerLayout.setBackgroundResource(backgroundResource);
        }
    }

    public void setTriangleIndicatorViewColor(int color) {
        mTriangleUpIndicatorView.setColor(color);
        mTriangleDownIndicatorView.setColor(color);
    }

    public void setBackgroundColor(int color) {
        mContainerLayout.setBackgroundColor(color);
    }

    public TriangleIndicatorView getTriangleUpIndicatorView() {
        return mTriangleUpIndicatorView;
    }

    public TriangleIndicatorView getTriangleDownIndicatorView() {
        return mTriangleDownIndicatorView;
    }

    public LinearLayout getContainerLayout() {
        return mContainerLayout;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
