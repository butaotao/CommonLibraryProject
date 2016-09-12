package com.dachen.community.presenters;

import com.dachen.community.contract.PageContact;
import com.dachen.community.data.PageSource;

/**
 * Created by RnMonkey on 16/9/11.
 */

public class PagePresenter implements PageContact.PagePresenter {

    private PageContact.PagerView mView;
    private PageSource mPageRepository;

    public PagePresenter(PageContact.PagerView mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mPageRepository.getPageDatas(0);
    }
}
