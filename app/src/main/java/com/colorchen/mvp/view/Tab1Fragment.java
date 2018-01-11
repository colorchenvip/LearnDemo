package com.colorchen.mvp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.colorchen.R;
import com.colorchen.ui.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 其他
 *
 * @author ChenQ
 * @time 2017/6/23 17:14
 * @emachenvip@163.com
 */
public class Tab1Fragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.tab1Button1)
    Button mTab1Button1;
    @BindView(R.id.tab1Button2)
    Button mTab1Button2;
    @BindView(R.id.tab1Button3)
    Button mTab1Button3;

    public static Tab1Fragment newInstance() {
        Bundle args = new Bundle();
//        args.putInt(Constants.TYPE, type);
        Tab1Fragment fragment = new Tab1Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_content_tab;
    }

    @Override
    protected void initViews() {
        mTab1Button1.setOnClickListener(this);
        mTab1Button2.setOnClickListener(this);
        mTab1Button3.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1Button1:
                Toast.makeText(getContext(), "button11", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab1Button2:
                Toast.makeText(getContext(), "button12", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab1Button3:
                Toast.makeText(getContext(), "button13", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.bind(this,getActivity()).unbind();
    }
}
