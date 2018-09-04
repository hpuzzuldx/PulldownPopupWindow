package com.ldx.pulldown;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ldx.pulldown.R;
import com.ldx.pulldown.popupwindow.PullDownMenu;
import com.ldx.pulldown.popupwindow.InputDialogView;
import com.ldx.pulldown.popupwindow.MenuItem;
import com.ldx.pulldown.nightmode.Main2Activity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PullDownMenu mPullDownMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPullDownMenu = new PullDownMenu(this);
        mPullDownMenu.setMenuList(getMenuList());
    }

    public void onClickPop(View view) {
        mPullDownMenu.show(view);
    }

    public void onClickPopWhite(View view) {
        PullDownMenu dropPopMenu = new PullDownMenu(this);
        dropPopMenu.setTriangleIndicatorViewColor(Color.WHITE);
        dropPopMenu.setBackgroundResource(R.drawable.dislike_popmenu_white_shap);

        dropPopMenu.setMenuList(getIconMenuList());
        dropPopMenu.show(view);
    }

    public void onClickPopIcon(View view) {
        PullDownMenu dropPopMenu = new PullDownMenu(this);
        dropPopMenu.setTriangleIndicatorViewColor(Color.WHITE);
        dropPopMenu.setBackgroundResource(R.drawable.dislike_popmenu_white_shap);
        dropPopMenu.setMenuList(getIconMenuList());

        dropPopMenu.show(view);
    }

    private ArrayList<MenuItem> getIconMenuList() {
        ArrayList<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem( 1, "选项1"));
        list.add(new MenuItem( 2, "选项2"));
        list.add(new MenuItem( 3, "选项3"));
        list.add(new MenuItem( 4, "选项4"));
        list.add(new MenuItem( 5, "选项5"));
        return list;
    }

    private ArrayList<MenuItem> getMenuList() {
        ArrayList<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem(1, "选项"));
        list.add(new MenuItem(2, "选项选项"));
        list.add(new MenuItem(3, "选项选项选项"));
        list.add(new MenuItem(4, "选项选项"));
        list.add(new MenuItem(5, "选项选项"));
        list.add(new MenuItem(5, "选项选项选"));
        return list;
    }

    public void onCommit(View view){
        PullDownMenu dropPopMenu = new PullDownMenu(this);
        dropPopMenu.setTriangleIndicatorViewColor(Color.WHITE);
        dropPopMenu.setBackgroundResource(R.drawable.dislike_popmenu_white_shap);
        dropPopMenu.setMenuList(getIconMenuList());
        dropPopMenu.show(view);

        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);

    }

    InputDialogView.SendBackListener sendBackListener = new InputDialogView.SendBackListener(){

        @Override
        public void sendBack(String inputText) {

        }
    };
}
