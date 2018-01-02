package com.xun.liverobot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xun.liverobot.R;
import com.xun.liverobot.json.JSONFormatExcetion;
import com.xun.liverobot.json.JSONToBeanHandler;
import com.xun.liverobot.mvp.model.bean.PatrolBean;
import com.xun.liverobot.sp.CommSetting;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class AddPatrolActivity extends AppCompatActivity {

    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_add_rebot)
    TextView tvAddRebot;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mqtt_ip_et)
    EditText mqttIpEt;
    @BindView(R.id.tencent_appid_et)
    EditText tencentAppidEt;
    @BindView(R.id.tencent_account_type_et)
    EditText tencentAccountTypeEt;
    @BindView(R.id.login_username_et)
    EditText loginUsernameEt;
    @BindView(R.id.login_password_et)
    EditText loginPasswordEt;
    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.start_room_et)
    EditText startRoomEt;
    @BindView(R.id.end_room_et)
    EditText endRoomEt;
    @BindView(R.id.start_patrol)
    Button startPatrol;
    @BindView(R.id.mqtt_port_et)
    EditText mqttPortEt;
    @BindView(R.id.loop_et)
    EditText loopEt;

    private PatrolBean patrolBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patrol);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            patrolBean = (PatrolBean) intent.getSerializableExtra("datas");
            if (patrolBean != null) {
                tvBarTitle.setText(patrolBean.getName());
                tvAddRebot.setVisibility(View.GONE);
                startPatrol.setVisibility(View.VISIBLE);
                nameEt.setText(patrolBean.getName());
                mqttIpEt.setText(patrolBean.getMqttIp());
                mqttPortEt.setText(String.valueOf(patrolBean.getMqttPort()));
                tencentAppidEt.setText(String.valueOf(patrolBean.getTencentAppId()));
                tencentAccountTypeEt.setText(String.valueOf(patrolBean.getTencentAccountType()));
                loginUsernameEt.setText(patrolBean.getLoginName());
                loginPasswordEt.setText(patrolBean.getLoginPwd());
                startRoomEt.setText(String.valueOf(patrolBean.getStartRoom()));
                endRoomEt.setText(String.valueOf(patrolBean.getEndRoom()));
                loopEt.setText(String.valueOf(patrolBean.getLoop()));
            }
        }
    }

    /**
     * @param type true为新增，false为修改
     * @return
     */
    private boolean addRobotInfoInDb(boolean type) {
        String name = nameEt.getText().toString();
        String mqttIp = mqttIpEt.getText().toString();
        String loginUsername = loginUsernameEt.getText().toString();
        String loginPassword = loginPasswordEt.getText().toString();
        int startRoom, endRoom, mqttPort, tencentAppid, tencentAccountType, loop;
        try {
            mqttPort = Integer.parseInt(mqttPortEt.getText().toString());
            startRoom = Integer.parseInt(startRoomEt.getText().toString());
            endRoom = Integer.parseInt(endRoomEt.getText().toString());
            tencentAppid = Integer.parseInt(tencentAppidEt.getText().toString());
            tencentAccountType = Integer.parseInt(tencentAccountTypeEt.getText().toString());
            loop = Integer.parseInt(loopEt.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "MQTT端口，开始，结束房间，腾讯相关，循环次数都必须为数字", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mqttIp) || TextUtils.isEmpty(loginUsername) || TextUtils.isEmpty(loginPassword)) {
            Toast.makeText(getApplicationContext(), "请确认信息填写完整", Toast.LENGTH_SHORT).show();
            return false;
        }

        Realm realm = Realm.getDefaultInstance();

        if (type) {
            realm.beginTransaction();
            int id = CommSetting.getCurrentMaxId() + 1;
            PatrolBean patrolBean = realm.createObject(PatrolBean.class, id);
            CommSetting.setCurrentMaxId(id);
            patrolBean.setName(name);
            patrolBean.setImgUrlIndex((int) (Math.random() * 100) % 5);
            patrolBean.setMqttIp(mqttIp);
            patrolBean.setMqttPort(mqttPort);
            patrolBean.setTencentAppId(tencentAppid);
            patrolBean.setTencentAccountType(tencentAccountType);
            patrolBean.setLoginName(loginUsername);
            patrolBean.setLoginPwd(loginPassword);
            patrolBean.setStartRoom(startRoom);
            patrolBean.setEndRoom(endRoom);
            patrolBean.setLoop(loop);
            realm.commitTransaction();
        } else {
            PatrolBean patrolBean = realm.where(PatrolBean.class).equalTo("id", CommSetting.getCurrentMaxId()).findFirst();
            realm.beginTransaction();
            patrolBean.setName(name);
            patrolBean.setMqttIp(mqttIp);
            patrolBean.setMqttPort(mqttPort);
            patrolBean.setTencentAppId(tencentAppid);
            patrolBean.setTencentAccountType(tencentAccountType);
            patrolBean.setLoginName(loginUsername);
            patrolBean.setLoginPwd(loginPassword);
            patrolBean.setStartRoom(startRoom);
            patrolBean.setEndRoom(endRoom);
            patrolBean.setLoop(loop);
            realm.commitTransaction();
        }
        if (patrolBean != null) {
            patrolBean.setName(name);
            patrolBean.setMqttIp(mqttIp);
            patrolBean.setMqttPort(mqttPort);
            patrolBean.setTencentAppId(tencentAppid);
            patrolBean.setTencentAccountType(tencentAccountType);
            patrolBean.setLoginName(loginUsername);
            patrolBean.setLoginPwd(loginPassword);
            patrolBean.setStartRoom(startRoom);
            patrolBean.setEndRoom(endRoom);
            patrolBean.setLoop(loop);
        }

        return true;
    }

    @OnClick(R.id.tv_add_rebot)
    public void addRebot() {
        if (addRobotInfoInDb(true)) {
            Toast.makeText(getApplicationContext(), "添加机器人成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @OnClick(R.id.start_patrol)
    public void startPatrol() {
        addRobotInfoInDb(false);

        try {
            String datas = JSONToBeanHandler.toJsonString(patrolBean);
            Intent intent = new Intent(AddPatrolActivity.this, PatrolingActivity.class);
            intent.putExtra("datas", datas);
            startActivity(intent);
        } catch (JSONFormatExcetion jsonFormatExcetion) {
            jsonFormatExcetion.printStackTrace();
        }
    }
}
