package com.colorchen.mvp.function.databinding;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.colorchen.R;
import com.colorchen.databinding.ActivityDataBindingItemBinding;
import com.colorchen.mvp.function.databinding.model.ItemViewModel;
import com.colorchen.mvp.function.databinding.model.MeiZiModel;

import java.util.List;

/**
 * 左右滑动的ViewPagerAdapter
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public class ScrollAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<MeiZiModel.Result> mDatas;

    public void setDatas(List<MeiZiModel.Result> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public ScrollAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ActivityDataBindingItemBinding binding = DataBindingUtil.inflate((LayoutInflater) mContext.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE), R.layout.activity_data_binding_item, null, false);
        ItemViewModel viewModel = new ItemViewModel(mContext);
        binding.setViewModel(viewModel);
        View rootView = binding.getRoot();
        container.addView(rootView, 0);
        viewModel.setData(mDatas.get(position), position % 2 == 0);
        return rootView;
    }
}
