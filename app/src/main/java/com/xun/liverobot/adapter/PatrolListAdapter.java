package com.xun.liverobot.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xun.liverobot.R;
import com.xun.liverobot.mvp.model.bean.PatrolBean;
import com.xun.liverobot.ui.AddPatrolActivity;

import java.util.List;

/**
 * Created by xunwang on 2017/12/28.
 */

public class PatrolListAdapter extends BaseQuickAdapter<PatrolBean, BaseViewHolder> {
    private Context context;
    private int[] imgs = {R.drawable.rebot1, R.drawable.rebot2, R.drawable.rebot3, R.drawable.rebot4, R.drawable.rebot5};

    public PatrolListAdapter(Context context, @Nullable List<PatrolBean> data) {
        super(R.layout.item_patrol_list, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final PatrolBean item) {
        helper.setText(R.id.tv_title, item.getId() + " " + item.getName());
        helper.setImageResource(R.id.iv_photo, imgs[item.getImgUrlIndex()]);
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPatrolActivity.class);
                intent.putExtra("datas", item);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
}
