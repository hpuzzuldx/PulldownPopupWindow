package com.ldx.pulldown.nightmode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldx.pulldown.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    LinearLayout mContainer;
    TextView textView ;
    Button button;
    ImageView imageView;
    RecyclerView mRecycleView;
    boolean isNight = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNight){
            setTheme(R.style.nightTheme);
        }else{
            setTheme(R.style.dayTheme);
        }

        setContentView(R.layout.activity_main2);
        mContainer = (LinearLayout) findViewById(R.id.container);
        textView = (TextView) findViewById(R.id.tv);
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image);
        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(Main2Activity.this);
        mRecycleView.setLayoutManager(manager);

        ArrayList<DemoBean> list = new ArrayList<>();
        list.add(new DemoBean( 1, "选项是的发送到111"));
        list.add(new DemoBean( 2, "选项所发生的2222"));
        list.add(new DemoBean( 3, "选项是的发送到3333"));
        list.add(new DemoBean( 4, "选项胜多负少4444"));
        list.add(new DemoBean( 5, "选项水电费水电费5555"));

        DemoAdapter mAdapter = new DemoAdapter(this,list);
        mRecycleView.setAdapter(mAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTheme();
            }
        });
    }

    private void changeTheme() {
        isNight = !isNight;
        if (isNight){
            setTheme(R.style.nightTheme);
        }else{
            setTheme(R.style.dayTheme);
        }
        showAnimation();
        TypedValue background = new TypedValue();//背景色
        TypedValue background2 = new TypedValue();//recycleView背景色
        TypedValue textColor = new TypedValue();//字体颜色
        TypedValue imagedemo = new TypedValue();//drawable
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.background2, background2, true);
        theme.resolveAttribute(R.attr.background1, background, true);
        theme.resolveAttribute(R.attr.textcolor1, textColor, true);
        theme.resolveAttribute(R.attr.drawdemo, imagedemo, true);

        mContainer.setBackgroundColor(background.data);
        textView.setTextColor(textColor.data);
        button.setTextColor(textColor.data);
        //直接的值需要直接获取
        button.setBackgroundColor(background.data);
        //来源于资源的可以获取
        imageView.setImageResource(imagedemo.resourceId);

        int childCount = mRecycleView.getChildCount();
       for (int i = 0;i<childCount;i++){
           View view = mRecycleView.getChildAt(i);
           LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.tv_text_container);
           TextView textView = (TextView) view.findViewById(R.id.tv_text);
           linearLayout.setBackgroundColor(background2.data);
           textView.setTextColor(textColor.data);
       }

       //清空缓存
        Class<RecyclerView> recyclerViewClass = RecyclerView.class;
        try {
            Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
            declaredField.setAccessible(true);
            Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(declaredField.get(mRecycleView), new Object[0]);
            RecyclerView.RecycledViewPool recycledViewPool = mRecycleView.getRecycledViewPool();
            recycledViewPool.clear();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


    /**
     * 展示一个切换动画
     */
    private void showAnimation() {
        final View decorView = getWindow().getDecorView();
        Bitmap cacheBitmap = getCacheBitmapFromView(decorView);
        if (decorView instanceof ViewGroup && cacheBitmap != null) {
            final View view = new View(this);
            view.setBackgroundDrawable(new BitmapDrawable(getResources(), cacheBitmap));
            ViewGroup.LayoutParams layoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) decorView).addView(view, layoutParam);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            objectAnimator.setDuration(1000);
            objectAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    ((ViewGroup) decorView).removeView(view);
                }
            });
            objectAnimator.start();
        }
    }

    /**
     * 获取一个 View 的缓存视图
     *
     * @param view
     * @return
     */
    private Bitmap getCacheBitmapFromView(View view) {
        final boolean drawingCacheEnabled = true;
        view.setDrawingCacheEnabled(drawingCacheEnabled);
        view.buildDrawingCache(drawingCacheEnabled);
        final Bitmap drawingCache = view.getDrawingCache();
        Bitmap bitmap;
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache);
            view.setDrawingCacheEnabled(false);
        } else {
            bitmap = null;
        }
        return bitmap;
    }

}
