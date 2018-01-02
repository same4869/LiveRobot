package com.xun.liverobot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xun.liverobot.R;
import com.xun.liverobot.adapter.LogListAdapter;
import com.xun.liverobot.adapter.PatrolLogListAdapter;
import com.xun.liverobot.mvp.model.bean.PatrolLogBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class LogListActivity extends AppCompatActivity {
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    LinearLayout refreshLayout;

    private LogListAdapter logListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_list);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            String time = intent.getStringExtra("time");
            List<PatrolLogBean> data = queryLogsByTime(time);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            logListAdapter = new LogListAdapter(data);
            recyclerView.setAdapter(logListAdapter);
        }
    }

    public List<PatrolLogBean> queryLogsByTime(String time) {
        Realm mRealm = Realm.getDefaultInstance();
        List<PatrolLogBean> logList = mRealm.where(PatrolLogBean.class).equalTo("date", time).findAll();
        return logList;
    }

}
