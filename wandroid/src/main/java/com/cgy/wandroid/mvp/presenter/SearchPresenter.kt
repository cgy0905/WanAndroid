package com.cgy.wandr

import com.cgy.wandroid.util.CacheUtil
import com.google.gson.Gson

()oid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.SearchContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.SearchResponse
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/20/2019 14:44
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class SearchPresenter
@Inject
constructor(model: SearchContract.Model, rootView: SearchContract.View) :
        BasePresenter<SearchContract.Model, SearchContract.View>(model, rootView) {
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

    fun getHotData() {
        mModel.getHotData()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<SearchResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<SearchResponse>>) {
                        if (response.isSucces()) {
                            CacheUtil.setSearchHistoryData(Gson().toJson(response.data))
                            mRootView.requestSearchSuccess(response.data)
                        } else {
                            mRootView.requestSearchSuccess(CacheUtil.getSearchData())
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestSearchSuccess(CacheUtil.getSearchData())
                    }
                })
    }
}
