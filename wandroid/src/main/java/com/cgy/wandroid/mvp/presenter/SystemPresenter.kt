package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.SystemContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.SystemResponse
import com.cgy.wandroid.util.CacheUtil
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject


@FragmentScope
class SystemPresenter
@Inject
constructor(model: SystemContract.Model, rootView: SystemContract.View) :
        BasePresenter<SystemContract.Model, SystemContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager


    fun getSystemData() {
        mModel.getSystemData()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<SystemResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<SystemResponse>>) {
                        if (response.isSucces()) {
                            CacheUtil.setSystemHistoryData(response.data)
                            mRootView.getSystemDataSuccess(response.data)
                        } else {
                            mRootView.getSystemDataSuccess(CacheUtil.getSystemHistoryData())
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.getSystemDataSuccess(CacheUtil.getSystemHistoryData())
                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
