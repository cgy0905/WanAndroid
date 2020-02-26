package com.cgy.wandroid.mvp.presenter

import android.app.Application

import com.cgy.wandroid.mvp.contract.ProjectContract
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
 * Created by MVPArmsTemplate on 09/19/2019 15:19
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class ProjectPresenter
@Inject
constructor(model: ProjectContract.Model, rootView: ProjectContract.View) :
        BasePresenter<ProjectContract.Model, ProjectContract.View>(model, rootView) {
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
        val data = CacheUtil.getProjectTitles()
        if (data.size != 0) {
            mRootView.requestTitleSuccess(data)
        }
        mModel.getTitles()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))   // activity的绑定方式 使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<ClassifyResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<ClassifyResponse>>) {
                        if (response.isSucces()) {
                            CacheUtil.setProjectTitles(Gson().toJson(response.data))
                            if (data.size == 0) {
                                mRootView.requestTitleSuccess(response.data)
                            }
                        } else {
                            if (data.size == 0) {
                                mRootView.requestTitleSuccess(data)
                            }
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        if (data.size == 0) {
                            mRootView.requestTitleSuccess(data)
                        }
                    }

                })
    }
}
