package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.PublicChildContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.util.HttpUtils
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
class PublicChildPresenter
@Inject
constructor(model: PublicChildContract.Model, rootView: PublicChildContract.View) :
        BasePresenter<PublicChildContract.Model, PublicChildContract.View>(model, rootView) {
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

    fun getPublicDataByType(pageNo: Int, cid: Int) {
        mModel.getPublicData(pageNo, cid)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>) {
                        if (response.isSucces()) {
                            mRootView.requestDataSuccess(response.data)
                        } else {
                            mRootView.requestDataFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestDataFailed(HttpUtils.getErrorText(t))
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
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))
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
                .retryWhen(RetryWithDelay(1,0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
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
}
