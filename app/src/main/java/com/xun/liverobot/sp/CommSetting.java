package com.xun.liverobot.sp;

/**
 * Created by xunwang on 2017/8/29.
 */

public class CommSetting {
    public static final String SETTING = "setting";
    private static final String SP_KEY_CURRENT_MAX_ID = "sp_current_max_id";
    private static final String PREFERENCE_KEY_NAME_TOKEN = "sp_key_name_token";
    private static final String SP_KEY_IS_RENDER = "sp_is_render";


    public static int getCurrentMaxId() {
        return PrefsMgr.getInt(SETTING, SP_KEY_CURRENT_MAX_ID, 0);
    }

    public static void setCurrentMaxId(int maxId) {
        PrefsMgr.putInt(SETTING, SP_KEY_CURRENT_MAX_ID, maxId);
    }

    public static String getToken() {
        return PrefsMgr.getString(SETTING, PREFERENCE_KEY_NAME_TOKEN, null);
    }

    public static void setToken(String token) {
        PrefsMgr.putString(SETTING, PREFERENCE_KEY_NAME_TOKEN, token);
    }

    public static boolean getIsRender() {
        return PrefsMgr.getBoolean(SETTING, SP_KEY_IS_RENDER, false);
    }

    public static void setIsRender(boolean isRender) {
        PrefsMgr.putBoolean(SETTING, SP_KEY_IS_RENDER, isRender);
    }

}
