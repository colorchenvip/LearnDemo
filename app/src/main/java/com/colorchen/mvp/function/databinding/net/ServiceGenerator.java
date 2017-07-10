package com.colorchen.mvp.function.databinding.net;

import com.colorchen.mvp.function.databinding.model.MeiZiModel;
import com.colorchen.mvp.function.databinding.inter.MeiZiCallBack;
import com.colorchen.net.UrlUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 回调获取返回结果
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public class ServiceGenerator {

    private static Retrofit retrofit =
            new Retrofit.Builder()
                    .baseUrl(UrlUtils.SERVER2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public static void getMeiZi(int page, int number, final MeiZiCallBack callBack) {
        MeiZiService service = retrofit.create(MeiZiService.class);
        Call<MeiZiModel> meiziCall = service.getMeiZi(page, number);
        meiziCall.enqueue(new Callback<MeiZiModel>() {
            @Override
            public void onResponse(Call<MeiZiModel> call, Response<MeiZiModel> response) {
                if (callBack != null) {
                    callBack.onSuccess(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MeiZiModel> call, Throwable t) {
                if (callBack != null) {
                    callBack.onFail(t.getMessage());
                }
            }
        });
    }
}
