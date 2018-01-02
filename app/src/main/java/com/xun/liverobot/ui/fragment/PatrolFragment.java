package com.xun.liverobot.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xun.liverobot.R;
import com.xun.liverobot.adapter.PatrolListAdapter;
import com.xun.liverobot.mvp.contract.PatrolContract;
import com.xun.liverobot.mvp.model.bean.PatrolBean;
import com.xun.liverobot.mvp.presenter.PatrolPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xunwang on 2017/12/28.
 */

public class PatrolFragment extends BaseFragment implements PatrolContract.View {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    LinearLayout refreshLayout;
    private PatrolPresenter mPresenter;
    private PatrolListAdapter patrolListAdapter;
    private List<PatrolBean> bean = new ArrayList<>();

    @Override
    protected void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        patrolListAdapter = new PatrolListAdapter(getContext(), bean);
        recyclerView.setAdapter(patrolListAdapter);
        mPresenter = new PatrolPresenter(this);
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter = new PatrolPresenter(this);
        mPresenter.start();
    }

    @Override
    protected int getLayoutResources() {
        return R.layout.patrol_fragment;
    }

    @Override
    public void setData(List<PatrolBean> bean) {
        patrolListAdapter.setNewData(bean);
    }

}
