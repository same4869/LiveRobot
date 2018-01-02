package com.xun.liverobot.manager;

import com.xun.liverobot.app.LiveRobotApplication;
import com.xun.liverobot.listener.ActionListener;
import com.xun.liverobot.listener.IReceivedMessageListener;
import com.xun.liverobot.mvp.model.bean.ReceivedMessage;
import com.xun.liverobot.utils.LogUtil;
import com.xun.liverobot.utils.StreamLogUtil;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Created by xunwang on 2017/12/29.
 */

public class MqttManager {
    public static final String TAG = "MqttManager";
    public static String TOPIC_ANDROID_DEBUG = "android/debug";

    private static MqttManager sInstance;
    private Connection sConnection = null;

    public static MqttManager getInstance() {
        if (sInstance == null) {
            synchronized (MqttManager.class) {
                if (sInstance == null) {
                    sInstance = new MqttManager();
                }
            }
        }
        return sInstance;
    }

    public boolean isConnected() {
        return sConnection.isConnected();
    }

    public Connection getConnection() {
        return sConnection;
    }

    public void connect(String name, String ip, int port) {
        if (sConnection != null) {
            try {
                sConnection.disconnect();
            } catch (RuntimeException e) {
                LogUtil.d(TAG, "disconnect last error " + e.getMessage());
            }
        }

        sConnection = Connection.createConnection("unique", name, ip, port, LiveRobotApplication.getInstance(), false);
        sConnection.addReceivedMessageListner(new IReceivedMessageListener() {
            @Override
            public void onMessageReceived(ReceivedMessage message) {

            }
        });

        String[] actionArgs = new String[1];
        actionArgs[0] = sConnection.getId();
        final ActionListener callback = new ActionListener(LiveRobotApplication.getInstance(), ActionListener.Action.CONNECT, sConnection, actionArgs) {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                super.onSuccess(asyncActionToken);
                StreamLogUtil.putLog("connect mqtt success, Thread : " + Thread.currentThread());
            }

            @Override
            public void onFailure(IMqttToken token, Throwable exception) {
                super.onFailure(token, exception);
                StreamLogUtil.putLog("connect mqtt failed exception, Thread : " + Thread.currentThread());
            }
        };
        sConnection.registerChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                LogUtil.d(TAG, "mqtt propertyChange event.getPropertyName() --> " + event.getPropertyName());
            }
        });

        sConnection.getClient().setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                LogUtil.d(TAG, "connection lost");

                sConnection.addAction("Connection lost", cause);
                sConnection.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                LogUtil.d(TAG, "mqtt messageArrived topic --> " + topic + " message --> " + message);
                sConnection.messageArrived(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // do nothing
            }
        });
        try {
            MqttConnectOptions options = new MqttConnectOptions();

            options.setMaxInflight(30);
            sConnection.addConnectionOptions(options);
            sConnection.getClient().connect(sConnection.getConnectionOptions(), null, callback);
        } catch (MqttException e) {
            LogUtil.d(TAG, "connection exception" + e.getMessage());
        }
    }

    public void disconnect() {
        if (sConnection != null) {
            try {
                sConnection.disconnect();
            } catch (RuntimeException e) {
                LogUtil.d(TAG, "disconnect last error " + e.getMessage());
            }
        }
    }

    public void publishReboot(int roomId) {
        publish(sConnection, TOPIC_ANDROID_DEBUG, "reboot,id=" + roomId, 0, false);
    }

    private void publish(Connection connection, final String topic, final String message, int qos, boolean retain) {

        LogUtil.d(TAG, String.format("publish topic = %s , message = %s , qos = %s , retain = %b", topic, message, qos, retain));

        if (connection == null || connection.getClient() == null || message == null) {
        } else if (!connection.isConnected()) {
        } else {
            try {
                String[] actionArgs = new String[2];
                actionArgs[0] = message;
                actionArgs[1] = topic;
                final ActionListener callback = new ActionListener(LiveRobotApplication.getInstance(),
                        ActionListener.Action.PUBLISH, connection, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        StreamLogUtil.putLog("发送重启命令成功 topic:" + topic + " message:" + message);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        StreamLogUtil.putLog("发送重启命令失败 topic:" + topic + " message:" + message);
                    }
                }, actionArgs);

                connection.getClient().publish(topic, message.getBytes(), qos, retain, null, callback);
            } catch (MqttException ex) {
            } catch (NullPointerException ex) {
            } catch (IllegalArgumentException ex) {
            } catch (Exception ex) {
            }
        }
    }
}
