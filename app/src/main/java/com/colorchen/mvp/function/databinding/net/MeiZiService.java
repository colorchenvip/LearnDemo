package com.colorchen.mvp.function.databinding.net;

import com.colorchen.mvp.function.databinding.model.MeiZiModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 使用retrofit 请求数据
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public interface MeiZiService {
    @GET("api/data/福利/{page}/{number}")
    Call<MeiZiModel> getMeiZi(@Path("page") int page, @Path("number") int number);
}
