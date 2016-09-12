package com.dachen.community;

import java.util.List;

/**
 * Created by RnMonkey on 16/9/11.
 */

public interface BaseView<T extends BasePresenter, D> {

    void setPresenter(T presenter);

    void onRefreshData(List<D> datas);

    void onLoadMoreData(List<D> datas);

    void showLoadingDialog();

    void hideLoadingDialog();
}
