package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.PublicContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ClassifyResponse
import com.cgy.wandroid.util.CacheUtil
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
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
 * Created by MVPArmsTemplate on 09/20/2019 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class PublicPresenter
@Inject
constructor(model: PublicContract.Model, rootView: PublicContract.View) :
        BasePresenter<PublicContract.Model, PublicContract.View>(model, rootView) {
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

    fun getProjectTitles() {
        val datas = CacheUtil.getPublicTitles()
        if (datas.size != 0) {
            mRootView.requesTitleSuccess(datas)
        }
        mModel.getTitles()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<ClassifyResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<ClassifyResponse>>) {
                        if (response.isSucces()) {
                            CacheUtil.setPublicTitles(Gson().toJson(response.data))
                            if (datas.size == 0) {
                                mRootView.requesTitleSuccess(response.data)
                            }
                        } else {
                            mRootView.requesTitleSuccess(datas)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        if (datas.size == 0) {
                            mRootView.requesTitleSuccess(datas)
                        }
                    }

                })
    }
}
