package com.xun.liverobot.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xun.liverobot.R;
import com.xun.liverobot.adapter.PatrolLogListAdapter;
import com.xun.liverobot.mvp.contract.PatrolLogContract;
import com.xun.liverobot.mvp.model.bean.PatrolLogBean;
import com.xun.liverobot.mvp.presenter.PatrolLogPresenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xunwang on 2018/1/1.
 */

public class PatrolLogFragment extends BaseFragment implements PatrolLogContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    LinearLayout refreshLayout;

    private PatrolLogPresenter mPresenter;
    private PatrolLogListAdapter patrolLogListAdapter;
    private List<String> listData = new ArrayList<>();

    @Override
    public void onLogs(List<PatrolLogBean> list) {
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            listData.add(list.get(i).getDate());
        }
        listData = new ArrayList<>(new HashSet(listData));
        patrolLogListAdapter.setNewData(listData);
    }

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        patrolLogListAdapter = new PatrolLogListAdapter(getContext(), listData);
        recyclerView.setAdapter(patrolLogListAdapter);
        mPresenter = new PatrolLogPresenter(this);
        mPresenter.requestLogs();
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.patrol_fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter = new PatrolLogPresenter(this);
        mPresenter.requestLogs();
    }
}
