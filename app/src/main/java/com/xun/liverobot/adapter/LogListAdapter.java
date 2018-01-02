package com.xun.liverobot.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xun.liverobot.R;
import com.xun.liverobot.mvp.model.bean.PatrolLogBean;

import java.util.List;

/**
 * Created by xunwang on 2018/1/2.
 */

public class LogListAdapter extends BaseQuickAdapter<PatrolLogBean, BaseViewHolder> {
    public LogListAdapter(@Nullable List<PatrolLogBean> data) {
        super(R.layout.item_log_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatrolLogBean item) {
        helper.setText(R.id.log_title, item.getLogStr());
    }
}
