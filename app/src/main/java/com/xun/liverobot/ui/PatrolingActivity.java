package com.xun.liverobot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveMemStatusLisenter;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.view.ILiveRootView;
import com.tencent.ilivesdk.view.VideoListener;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVText;
import com.xun.liverobot.R;
import com.xun.liverobot.app.LiveRobotApplication;
import com.xun.liverobot.config.Constants;
import com.xun.liverobot.json.JSONFormatExcetion;
import com.xun.liverobot.json.JSONToBeanHandler;
import com.xun.liverobot.manager.MqttManager;
import com.xun.liverobot.mvp.contract.PatrolingContract;
import com.xun.liverobot.mvp.model.bean.EmailLoginBean;
import com.xun.liverobot.mvp.model.bean.PatrolBean;
import com.xun.liverobot.mvp.model.bean.PatrolLogBean;
import com.xun.liverobot.mvp.presenter.PatrolingPresenter;
import com.xun.liverobot.sp.CommSetting;
import com.xun.liverobot.utils.LogUtil;
import com.xun.liverobot.utils.StreamLogUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import commlib.xun.com.commlib.CommCountDownTimer;
import io.realm.Realm;

public class PatrolingActivity extends AppCompatActivity implements PatrolingContract.View {

    @BindView(R.id.game_master_view)
    ILiveRootView mMasterView;
    @BindView(R.id.game_slave_view)
    ILiveRootView mSlaveView;
    @BindView(R.id.out_log_tv)
    TextView outLogTv;
    @BindView(R.id.out_log_sv)
    ScrollView outLogSv;

    private PatrolingPresenter patrolingPresenter;
    private PatrolBean item;
    private boolean isMRevFirstFrame, isSRevFirstFrame;
    private CommCountDownTimer commCountDownTimer;
    private int currentRoomId, startRoomId, endRoomId;
    private int loop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_patroling);
        ButterKnife.bind(this);

        initStreamLog();
        initData();
    }

    private void initData() {
        patrolingPresenter = new PatrolingPresenter(this);
        Intent intent = getIntent();
        if (intent != null) {
            String datas = intent.getStringExtra("datas");
            try {
                item = JSONToBeanHandler.fromJsonString(datas, PatrolBean.class);
                if (item != null) {
                    StreamLogUtil.putLog("开始连接mqtt,name:" + item.getName() + " mqttip:" + item.getMqttIp() + " mqttport:" + item.getMqttPort());
                    MqttManager.getInstance().connect(item.getName(), item.getMqttIp(), item.getMqttPort());
                    StreamLogUtil.putLog("开始登录服务器,username:" + item.getLoginName() + " password:" + item.getLoginPwd());
                    currentRoomId = item.getStartRoom() - 1;
                    startRoomId = item.getStartRoom();
                    endRoomId = item.getEndRoom();
                    loop = item.getLoop();
                    patrolingPresenter.requestLogin(item.getLoginName(), item.getLoginPwd());
                }
            } catch (JSONFormatExcetion jsonFormatExcetion) {
                jsonFormatExcetion.printStackTrace();
            }
        }
    }

    private void initStreamLog() {
        StreamLogUtil.initLog(getMainLooper(), new StreamLogUtil.OnRecvLog() {
            @Override
            public void onRecvLog(String log) {
                outLogTv.setText(StreamLogUtil.rebuildOutLog());
                outLogSv.fullScroll(ScrollView.FOCUS_DOWN);
            }

            @Override
            public void onStateChange(String state) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StreamLogUtil.clearLog();
        MqttManager.getInstance().disconnect();
        ILiveRoomManager.getInstance().quitRoom(null);
        ILiveLoginManager.getInstance().iLiveLogout(null);
        if (commCountDownTimer != null) {
            commCountDownTimer.cancel();
        }
        ILiveRoomManager.getInstance().onDestory();
    }

    @Override
    public void onLoginData(EmailLoginBean bean) {
        if (bean != null) {
            if (bean.getCode() == 0) {
                StreamLogUtil.putLog("登录成功");
                CommSetting.setToken(bean.getData().getSessionId());
                StreamLogUtil.putLog("开始初始化腾讯sdk appid --> " + item.getTencentAppId() + " accounttype --> " + item.getTencentAccountType());
                initTecentSdk(item.getTencentAppId(), item.getTencentAccountType());
                StreamLogUtil.putLog("开始登陆成功腾讯云 mLiveId --> " + bean.getData().getLive().getIdentifier() + " mLiveSig --> " + bean.getData().getLive().getSign());
                loginTecentSdk(bean.getData().getLive().getIdentifier(), bean.getData().getLive().getSign());
            } else {
                StreamLogUtil.putLog("登录失败 code --> " + bean.getCode() + " msg --> " + bean.getMsg());
            }
        }
    }

    private void loginTecentSdk(String mLiveId, String mLiveSig) {
        ILiveLoginManager.getInstance().iLiveLogin(mLiveId, mLiveSig, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                StreamLogUtil.putLog("腾讯云登陆成功,开始加入房间");
                ILiveSDK.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        startJoinRoom(getNextRoomId(), false);
                    }
                }, 0);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
            }
        });
    }

    private int getNextRoomId() {
        currentRoomId++;
        if (currentRoomId <= endRoomId) {
            return currentRoomId;
        } else {
            ILiveRoomManager.getInstance().quitRoom(null);
            loop--;
            StreamLogUtil.putLog("---本轮轮询结束，还剩" + loop + "轮轮询---");
            logInToDB(0, "---本轮轮询结束，还剩" + loop + "轮轮询---");
            if (loop > 0) {
                currentRoomId = startRoomId;
                return currentRoomId;
            }
        }
        return -1;
    }

    /**
     * @param roomId
     * @param flag   正常用false，如果有异常用true
     */
    private void startJoinRoom(final int roomId, final boolean flag) {
        ILiveSDK.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {

                StreamLogUtil.putLog("加入房间 roomId --> " + roomId);
                if (roomId == -1) {
                    StreamLogUtil.putLog("当前巡逻完毕");
                    return;
                }
                isMRevFirstFrame = false;
                isSRevFirstFrame = false;

                //这里的ControlRole分为两种，观看时是Guest，视频延迟较大，等开始游戏上麦之后，切换为LiveGuest，这时候视频延迟会变小
                final ILiveRoomOption option = new ILiveRoomOption(ILiveLoginManager.getInstance().getMyUserId())
                        .controlRole("Guest")
                        .autoCamera(false)
                        .autoMic(false)
                        .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                        .setRoomMemberStatusLisenter(new ILiveMemStatusLisenter() {
                            @Override
                            public boolean onEndpointsUpdateInfo(int eventid, String[] updateList) {
                                StreamLogUtil.putLog("房间里有" + updateList.length + "个人");
                                for (int i = 0; i < updateList.length; i++) {
                                    StreamLogUtil.putLog("房间成员" + updateList[i]);
                                }
                                return false;
                            }
                        });
                if (roomId != startRoomId && !flag) {
                    ILiveRoomManager.getInstance().switchRoom(roomId, option, new ILiveCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            onJoinSuc(String.valueOf(roomId));
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            StreamLogUtil.putLog("腾讯云加入" + roomId + "号房间失败，errCode --> " + errCode + " errMsg --> " + errMsg);
                            logInToDB(roomId, "腾讯云加入" + roomId + "号房间失败，errCode --> " + errCode + " errMsg --> " + errMsg);
                            if (errCode != 1003) {
                                MqttManager.getInstance().publishReboot(currentRoomId);
                            }
                            startJoinRoom(getNextRoomId(), true);
                        }
                    });
                } else {
                    ILiveRoomManager.getInstance().joinRoom(roomId, option, new ILiveCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            onJoinSuc(String.valueOf(roomId));
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            StreamLogUtil.putLog("腾讯云加入" + roomId + "号房间失败，errCode --> " + errCode + " errMsg --> " + errMsg);
                            logInToDB(roomId, "腾讯云加入" + roomId + "号房间失败，errCode --> " + errCode + " errMsg --> " + errMsg);
                            if (errCode != 1003) {
                                MqttManager.getInstance().publishReboot(currentRoomId);
                            }
                            startJoinRoom(getNextRoomId(), true);
                        }
                    });
                }
            }
        }, 2000);
    }

    private void initTecentSdk(int appId, int accountType) {
        //iLiveSDK初始化
        ILiveSDK.getInstance().initSdk(LiveRobotApplication.getInstance(), appId, accountType);
        TIMManager.getInstance().setLogLevel(TIMLogLevel.DEBUG);
        //初始化直播场景
        ILVLiveConfig liveConfig = new ILVLiveConfig();
        liveConfig.setLiveMsgListener(new ILVLiveConfig.ILVLiveMsgListener() {
            @Override
            public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
                LogUtil.d(Constants.TAG, "onNewTextMsg : " + text.getText());
            }

            @Override
            public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
                LogUtil.d(Constants.TAG, "cmd " + cmd);
            }

            @Override
            public void onNewOtherMsg(TIMMessage message) {
                LogUtil.d(Constants.TAG, "message " + message);
            }
        });
        ILVLiveManager.getInstance().init(liveConfig);
    }

    private void onJoinSuc(String roomId) {
        StreamLogUtil.putLog("腾讯云加入" + roomId + "号房间成功");
        initMSView();
        if (commCountDownTimer != null) {
            commCountDownTimer.cancel();
        }
        commCountDownTimer = new CommCountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                StreamLogUtil.putLog("检查倒计时：" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if (isMRevFirstFrame && isSRevFirstFrame) {
                    StreamLogUtil.putLog("主副摄像头推流都正常");
                    logInToDB(currentRoomId, "主副摄像头推流都正常");
                } else {
                    StreamLogUtil.putLog("isMRevFirstFrame --> " + isMRevFirstFrame + " isSRevFirstFrame --> " + isSRevFirstFrame + " 重启盒子 currentRoomId --> " + currentRoomId);
                    logInToDB(currentRoomId, "isMRevFirstFrame --> " + isMRevFirstFrame + " isSRevFirstFrame --> " + isSRevFirstFrame + " 重启盒子 currentRoomId --> " + currentRoomId);
                    MqttManager.getInstance().publishReboot(currentRoomId);
                }
                ILiveSDK.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        startJoinRoom(getNextRoomId(), false);
                    }
                }, 0);
            }
        }.start();
    }

    private void initMSView() {
        //UI初始化完成之后，将三个用于视频显示的控件传给SDK进行初始化
        ILiveRoomManager.getInstance().initRootViewArr(new ILiveRootView[]{mMasterView, mSlaveView});

        if (mMasterView != null && mSlaveView != null) {
            mMasterView.render("m_" + currentRoomId, AVView.VIDEO_SRC_TYPE_CAMERA);
            mSlaveView.render("s01_" + currentRoomId, AVView.VIDEO_SRC_TYPE_CAMERA);
        }

        if (CommSetting.getIsRender()) {
            mMasterView.setVisibility(View.VISIBLE);
        }

        mMasterView.setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                StreamLogUtil.putLog("主摄像头收到第一帧");
                isMRevFirstFrame = true;
            }

            @Override
            public void onHasVideo(String s, int i) {
                LogUtil.d(Constants.TAG, "mMasterView onHasVideo s --> " + s + " i --> " + i);
                if (s != null && s.equals("m_" + currentRoomId)) {
                    isMRevFirstFrame = true;
                }
            }

            @Override
            public void onNoVideo(String s, int i) {
                LogUtil.d(Constants.TAG, "mMasterView onNoVideo s --> " + s + " i --> " + i);
            }

        });
        mSlaveView.setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                StreamLogUtil.putLog("侧摄像头收到第一帧");
                isSRevFirstFrame = true;
            }

            @Override
            public void onHasVideo(String s, int i) {
                LogUtil.d(Constants.TAG, "mSlaveView onHasVideo s --> " + s + " i --> " + i);
                if (s != null && s.equals("s01_" + currentRoomId)) {
                    isSRevFirstFrame = true;
                }
            }

            @Override
            public void onNoVideo(String s, int i) {
                LogUtil.d(Constants.TAG, "mSlaveView onNoVideo s --> " + s + " i --> " + i);
            }

        });
    }

    private void logInToDB(int roomId, String log) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PatrolLogBean patrolLogBean = realm.createObject(PatrolLogBean.class);
        patrolLogBean.setDate("" + year + "" + month + "" + day);
        patrolLogBean.setLogStr("" + year + month + day + " " + hour + ":" + minute + ":" + second + " roomId:" + roomId + " " + log);
        realm.commitTransaction();
    }
}
