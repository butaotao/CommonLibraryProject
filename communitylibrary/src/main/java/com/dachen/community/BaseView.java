package com.dachen.community;

/**
 * Created by RnMonkey on 16/9/11.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

    void showLoadingDialog();

    void hideLoadingDialog();
}
