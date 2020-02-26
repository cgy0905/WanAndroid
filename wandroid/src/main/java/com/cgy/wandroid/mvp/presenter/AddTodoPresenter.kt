package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.AddTodoContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
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
class AddTodoPresenter
@Inject
constructor(model: AddTodoContract.Model, rootView: AddTodoContract.View) :
        BasePresenter<AddTodoContract.Model, AddTodoContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    fun addTodo(title: String, content: String, date: String, priority: Int) {
        mModel.addTodo(title, content, date, 0, priority)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .doOnSubscribe {
                    mRootView.showLoading()//显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mRootView.hideLoading()//隐藏加载框
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            mRootView.addTodoSuccess()
                        } else {
                            mRootView.addTodoFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.addTodoFailed(HttpUtils.getErrorText(t))
                    }
                })
    }

    fun updateTodo(title: String, content: String, date: String, priority: Int, id: Int) {
        mModel.updateTodo(title, content, date, 0, priority, id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .doOnSubscribe {
                    mRootView.showLoading() //显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    mRootView.hideLoading() //隐藏加载框
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            mRootView.addTodoSuccess()
                        } else {
                            mRootView.addTodoFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.addTodoFailed(HttpUtils.getErrorText(t))
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
