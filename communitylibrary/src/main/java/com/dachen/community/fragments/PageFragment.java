package com.dachen.community.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.dachen.community.contract.PageContact;
import com.dachen.community.data.bean.PageData;

import java.util.List;

/**
 * Created by RnMonkey on 16/9/11.
 */

public class PageFragment extends Fragment implements PageContact.PagerView {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PageContact.PagePresenter presenter;

    @Override
    public void setPresenter(PageContact.PagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onRefreshData(List<PageData> datas) {

    }

    @Override
    public void onLoadMoreData(List<PageData> datas) {

    }

    @Override
    public void showLoadingDialog() {

    }

    @Override
    public void hideLoadingDialog() {

    }
}
