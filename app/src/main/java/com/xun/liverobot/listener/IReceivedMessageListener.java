package com.xun.liverobot.listener;

import com.xun.liverobot.mvp.model.bean.ReceivedMessage;

/**
 * Created by xunwang on 2017/12/29.
 */

public interface IReceivedMessageListener {
    void onMessageReceived(ReceivedMessage message);
}
