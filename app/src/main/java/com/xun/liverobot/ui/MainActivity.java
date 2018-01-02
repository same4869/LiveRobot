package com.xun.liverobot.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.xun.liverobot.R;
import com.xun.liverobot.ui.fragment.PatrolFragment;
import com.xun.liverobot.ui.fragment.PatrolLogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_find)
    RadioButton rbFind;
    @BindView(R.id.rb_hot)
    RadioButton rbHot;
    @BindView(R.id.rg_root)
    RadioGroup rgRoot;
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    private PatrolFragment patrolFragment;
    private PatrolLogFragment patrolLogFragment;
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ImmersionBar.with(this).transparentBar().barAlpha(0.3f).fitsSystemWindows(true).init();

        setRadioButton();
        initToolbar();
        initFragment();
    }

    private void setRadioButton() {
        rbHome.setChecked(true);
        rbHome.setTextColor(Color.BLACK);
    }

    private void initToolbar() {
        tvBarTitle.setText("巡逻机器人");
        tvBarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Lobster-1.4.otf"));
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentIndex == 0) {
                    Intent intent = new Intent(MainActivity.this, AddPatrolActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void initFragment() {
        patrolFragment = new PatrolFragment();
        patrolLogFragment = new PatrolLogFragment();
        FragmentTransaction fragmentTrans = getSupportFragmentManager().beginTransaction();
        fragmentTrans.add(R.id.fl_content, patrolFragment);
        fragmentTrans.add(R.id.fl_content, patrolLogFragment);
        fragmentTrans.hide(patrolLogFragment);
        fragmentTrans.commit();
    }

    @OnClick(R.id.rb_home)
    public void onClickHome() {
        currentIndex = 0;
        clearState();
        rbHome.setChecked(true);
        rbHome.setTextColor(Color.BLACK);
        getSupportFragmentManager().beginTransaction().show(patrolFragment)
                .hide(patrolLogFragment)
                .commit();
        ivSearch.setImageResource(R.drawable.share_qq_d);
        tvBarTitle.setText("巡逻");
    }

    @OnClick(R.id.rb_find)
    public void onClickFind() {
        currentIndex = 1;
        clearState();
        rbFind.setChecked(true);
        rbFind.setTextColor(Color.BLACK);
        getSupportFragmentManager().beginTransaction().show(patrolLogFragment)
                .hide(patrolFragment)
                .commit();
        ivSearch.setImageResource(R.drawable.icon_setting);
        tvBarTitle.setText("日志");
    }

    private void clearState() {
        rgRoot.clearCheck();
        rbHome.setTextColor(Color.GRAY);
        rbHot.setTextColor(Color.GRAY);
        rbFind.setTextColor(Color.GRAY);
    }
}
