package com.xun.liverobot.mvp.model.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by xunwang on 2017/12/28.
 */

public class PatrolBean extends RealmObject implements Serializable {
    @PrimaryKey
    private int id;
    private String name;
    private int imgUrlIndex;
    private String mqttIp;
    private int mqttPort;
    private int tencentAppId;
    private int tencentAccountType;
    private String loginName;
    private String loginPwd;
    private int startRoom;
    private int endRoom;
    private int loop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgUrlIndex() {
        return imgUrlIndex;
    }

    public void setImgUrlIndex(int imgUrlIndex) {
        this.imgUrlIndex = imgUrlIndex;
    }

    public String getMqttIp() {
        return mqttIp;
    }

    public void setMqttIp(String mqttIp) {
        this.mqttIp = mqttIp;
    }

    public int getMqttPort() {
        return mqttPort;
    }

    public void setMqttPort(int mqttPort) {
        this.mqttPort = mqttPort;
    }

    public int getTencentAppId() {
        return tencentAppId;
    }

    public void setTencentAppId(int tencentAppId) {
        this.tencentAppId = tencentAppId;
    }

    public int getTencentAccountType() {
        return tencentAccountType;
    }

    public void setTencentAccountType(int tencentAccountType) {
        this.tencentAccountType = tencentAccountType;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public int getStartRoom() {
        return startRoom;
    }

    public void setStartRoom(int startRoom) {
        this.startRoom = startRoom;
    }

    public int getEndRoom() {
        return endRoom;
    }

    public void setEndRoom(int endRoom) {
        this.endRoom = endRoom;
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }
}
