package com.colorchen.mvp.function.databinding;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.colorchen.R;
import com.colorchen.mvp.function.databinding.inter.MeiZiCallBack;
import com.colorchen.mvp.function.databinding.model.MeiZiModel;
import com.colorchen.mvp.function.databinding.net.ServiceGenerator;
import com.colorchen.ui.widget.BannerViewPager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * dataBinding 使用
 *
 * @author ChenQ
 * @time 2017/7/10 10:34
 * @email：colorchenvip@163.com
 */
public class DataBindingActivity extends AppCompatActivity {

    @Bind(R.id.indexViewPager)
    BannerViewPager mIndexViewPager;
    private ScrollAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_binding);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        adapter = new ScrollAdapter(this);
        mIndexViewPager.setAdapter(adapter);
        //http://gank.io/api/data/福利/{11}/{2}
        ServiceGenerator.getMeiZi(11, 1, new MeiZiCallBack() {
            @Override
            public void onSuccess(List<MeiZiModel.Result> result) {
                adapter.setDatas(result);
                Log.i("dataBinding","请求数据成功");
            }

            @Override
            public void onFail(String error) {
                //TODO 没处理失败的情况。
                Log.i("dataBinding","请求数据fail");
            }
        });

       /* OkGo.post("http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1").execute(new DialogCallback<List<MeiZiModel.Result>>(this) {
            @Override
            public void onSuccess(List<MeiZiModel.Result> responseData, Call call, Response response) {
                adapter.setDatas(responseData);
                Log.i("dataBinding","请求数据成功");
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.i("dataBinding","请求数据fail"+e.toString());
            }
        });*/
    }
}
