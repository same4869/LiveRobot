package com.xun.liverobot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xun.liverobot.R;
import com.xun.liverobot.ui.LogListActivity;

import java.util.List;

/**
 * Created by xunwang on 2018/1/1.
 */

public class PatrolLogListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;

    public PatrolLogListAdapter(Context context, @Nullable List<String> data) {
        super(R.layout.item_patrol_log_list, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        helper.setText(R.id.log_title, item);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LogListActivity.class);
                intent.putExtra("time", item);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
