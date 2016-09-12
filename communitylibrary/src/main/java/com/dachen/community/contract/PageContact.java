package com.dachen.community.contract;

import com.dachen.community.BasePresenter;
import com.dachen.community.BaseView;
import com.dachen.community.data.bean.PageData;

/**
 * Created by RnMonkey on 16/9/11.
 *  契约类,定义对应的present的需要实现的方法,以及view中需要实现的方法。
 */

public interface PageContact {

    interface PagerView extends BaseView<PagePresenter, PageData> {



    }

    interface PagePresenter extends BasePresenter {

    }
}
