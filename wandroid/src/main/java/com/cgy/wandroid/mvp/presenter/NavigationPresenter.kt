package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.NavigationContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.NavigationResponse
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
class NavigationPresenter
@Inject
constructor(model: NavigationContract.Model, rootView: NavigationContract.View) :
        BasePresenter<NavigationContract.Model, NavigationContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    fun getNavigationData() {
        mModel.getNavigationData()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<NavigationResponse>>>(mErrorHandler){
                    override fun onNext(response: ApiResponse<MutableList<NavigationResponse>>) {
                        if (response.isSucces()) {
                            //请求成功,保存数据
                            CacheUtil.setNavigationHistoryData(response.data)
                            //回调数据给activity
                            mRootView.getNavigationDataSuccess(response.data)
                        } else {
                            //请求失败,回调缓存数据给activity
                            mRootView.getNavigationDataSuccess(CacheUtil.getNavigationHistoryData())
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //请求失败,回调缓存数据给activity
                        mRootView.getNavigationDataSuccess(CacheUtil.getNavigationHistoryData())
                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
