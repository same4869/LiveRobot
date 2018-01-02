package com.xun.liverobot.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.xun.liverobot.R;
import com.xun.liverobot.mvp.model.bean.PatrolLogBean;
import com.xun.liverobot.sp.CommSetting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.setting_render)
    Switch settingRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        settingRender.setChecked(CommSetting.getIsRender());
        settingRender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CommSetting.setIsRender(isChecked);
            }
        });
    }

    @OnClick(R.id.delete_log)
    public void deleteLog() {
        Realm mRealm = Realm.getDefaultInstance();
        final RealmResults<PatrolLogBean> logs = mRealm.where(PatrolLogBean.class).findAll();

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                logs.deleteAllFromRealm();
                Toast.makeText(getApplicationContext(), "删除全部日志成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
