package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.util.HttpUtils
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


    fun getArticleList(pageNo: Int, searchKey: String) {
        mModel.getArticleList(pageNo, searchKey)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>) {
                        if (response.isSucces()) {
                            mRootView.requestArticleSuccess(response.data)
                        } else {
                            mRootView.requestArticleFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestArticleFailed(HttpUtils.getErrorText(t))
                    }

                })
    }

    /**
     * 取消收藏
     */
    fun unCollect(id: Int, position: Int) {
        mModel.unCollect(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            //取消收藏成功
                            mRootView.collect(false, position)
                        } else {
                            //取消收藏失败
                            mRootView.collect(true, position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //取消收藏失败
                        mRootView.collect(true, position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }

                })
    }

    /**
     * 收藏
     */
    fun collect(id: Int, position: Int) {
        mModel.collect(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            //收藏成功
                            mRootView.collect(true, position)
                        } else {
                            //收藏失败
                            mRootView.collect(false, position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //收藏失败
                        mRootView.collect(false, position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}

