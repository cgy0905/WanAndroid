package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.ShareByIdContract
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject


@ActivityScope
class ShareByIdPresenter
@Inject
constructor(model: ShareByIdContract.Model, rootView: ShareByIdContract.View) :
        BasePresenter<ShareByIdContract.Model, ShareByIdContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager


    override fun onDestroy() {
        super.onDestroy()
    }

    fun getShareData(pageNo: Int, id: Int) {

    }

    fun unCollect(id: Int, position: Int) {

    }

    fun collect(id: Int, position: Int) {

    }
}
