package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject


@ActivityScope
class SearchResultPresenter
@Inject
constructor(model: SearchResultContract.Model, rootView: SearchResultContract.View) :
BasePresenter<SearchResultContract.Model, SearchResultContract.View>(model,rootView) {
    @Inject
    lateinit var mErrorHandler:RxErrorHandler
    @Inject
    lateinit var mApplication:Application
    @Inject
    lateinit var mImageLoader:ImageLoader
    @Inject
    lateinit var mAppManager:AppManager


    override fun onDestroy() {
          super.onDestroy()
    }

    fun getArticleList(pageNo: Int, searchKey: String?) {

    }

    fun unCollect(id: Int, position: Int) {

    }

    fun collect(id: Int, position: Int) {

    }


}

