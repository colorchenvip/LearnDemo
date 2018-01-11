package com.colorchen.mvp.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.colorchen.MainActivity;
import com.colorchen.R;
import com.colorchen.ui.BaseFragment;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.animate.FlipAnimater;
import com.jpeng.jptabbar.animate.JumpAnimater;
import com.jpeng.jptabbar.animate.RotateAnimater;
import com.jpeng.jptabbar.animate.ScaleAnimater;

import butterknife.BindView;

/**
 * tabBar 控件的设置
 *
 * @author ChenQ
 * @time 2017/6/23 17:14
 * @email：colorchenvi3.com
 */
public class TabBarSettingFragment extends BaseFragment implements View.OnClickListener, TextWatcher, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.et_count)
    EditText mNumberEt;
    @BindView(R.id.imageButton1)
    ImageButton mMinusIb;
    @BindView(R.id.imageButton2)
    ImageButton mPlusIb;

    @BindView(R.id.button1)
    Button mShowTextBtn;
    @BindView(R.id.button2)
    Button mShowCircleBtn;
    @BindView(R.id.button3)
    Button mHideBtn;

    JPTabBar mTabBar;
    @BindView(R.id.radioGroup)
    RadioGroup mGroup;
    @BindView(R.id.radioGroup1)
    RadioGroup mRadioGroup1;
    @BindView(R.id.radioGroup2)
    RadioGroup mRadioGroup2;

    public static TabBarSettingFragment newInstance() {
        Bundle args = new Bundle();
//        args.putInt(Constants.TYPE, type);
        TabBarSettingFragment fragment = new TabBarSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_tabbar_setting;
    }

    @Override
    protected void initViews() {
        mTabBar = ((MainActivity) getActivity()).getTaBar();
        mShowTextBtn.setOnClickListener(this);
        mShowCircleBtn.setOnClickListener(this);
        mHideBtn.setOnClickListener(this);
        mNumberEt.addTextChangedListener(this);
        mPlusIb.setOnClickListener(this);
        mMinusIb.setOnClickListener(this);
        mGroup.setOnCheckedChangeListener(this);
        mRadioGroup1.setOnCheckedChangeListener(this);
        mRadioGroup2.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        int count = Integer.parseInt(mNumberEt.getText().toString());
        if (v == mMinusIb) {
            count--;
            mNumberEt.setText(count + "");
        } else if (v == mPlusIb) {
            count++;
            mNumberEt.setText(count + "");
        } else if (v == mShowTextBtn) {
            mTabBar.showBadge(1, "文字");
        } else if (v == mShowCircleBtn) {
            mTabBar.showCircleBadge(1);
        } else {
            mTabBar.hideBadge(1);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (s.toString().equals("")) {
            mTabBar.showBadge(1, 0);
            return;
        }
        int count = Integer.parseInt(s.toString());
        if (mTabBar != null)
            mTabBar.showBadge(1, count, true);
    }

    public void clearCount() {
        if (mNumberEt != null) {
            mNumberEt.setText("0");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radioButton1:
                mTabBar.setCustomAnimate(new ScaleAnimater());
                break;
            case R.id.radioButton2:
                mTabBar.setCustomAnimate(new JumpAnimater());
                break;
            case R.id.radioButton3:
                mTabBar.setCustomAnimate(new FlipAnimater());
                break;
            case R.id.radioButton4:
                mTabBar.setCustomAnimate(new RotateAnimater());
                break;
            case R.id.radioButton5:
                mTabBar.setUseScrollAnimate(true);
                break;
            case R.id.radioButton6:
                mTabBar.setUseScrollAnimate(false);
                break;
            case R.id.radioButton7:
                mTabBar.setUseFilter(true);
                break;
            case R.id.radioButton8:
                mTabBar.setUseFilter(false);
                break;
        }
    }

}
