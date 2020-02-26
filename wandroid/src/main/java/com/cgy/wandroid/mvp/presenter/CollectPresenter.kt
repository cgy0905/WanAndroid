package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.CollectContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.CollectResponse
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
class CollectPresenter
@Inject
constructor(model: CollectContract.Model, rootView: CollectContract.View) :
        BasePresenter<CollectContract.Model, CollectContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    fun getCollectDataByType(pageNo: Int) {
        mModel.getCollectData(pageNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<CollectResponse>>>>(mErrorHandler){
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<CollectResponse>>>) {
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



    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * 取消收藏
     */
    fun unCollect(id: Int, originId: Int, position: Int) {
        mModel.unCollectList(id, originId)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            //取消收藏成功
                            mRootView.unCollect(position)
                        } else {
                            //取消收藏失败
                            mRootView.unCollectFailed(position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //取消收藏失败
                        mRootView.unCollectFailed(position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })

    }
}
