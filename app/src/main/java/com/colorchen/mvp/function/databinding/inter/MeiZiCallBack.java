package com.colorchen.mvp.function.databinding.inter;

import com.colorchen.mvp.function.databinding.model.MeiZiModel;

import java.util.List;

/**
 * Author ChenQ on 2017/7/10
 * email：wxchenq@yutong.com
 */
public interface MeiZiCallBack {
    void onSuccess(List<MeiZiModel.Result> results);
    void onFail(String error);
}
