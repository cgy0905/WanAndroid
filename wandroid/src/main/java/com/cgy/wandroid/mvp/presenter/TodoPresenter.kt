package com.cgy.wandroid.mvp.presenter

import android.app.Application
import com.cgy.wandroid.mvp.contract.TodoContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.TodoResponse
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
class TodoPresenter
@Inject
constructor(model: TodoContract.Model, rootView: TodoContract.View) :
        BasePresenter<TodoContract.Model, TodoContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    /**
     * 获取待办任务数据
     */
    fun getTodoData(pageNo: Int) {
        mModel.getTodoData(pageNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>(mErrorHandler){
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>) {
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

    fun delTodo(id: Int, position: Int) {
        mModel.deleteTodoData(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .doOnSubscribe {
                    mRootView.showLoading() //显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mRootView.hideLoading() //隐藏加载框
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler){
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            mRootView.deleteTodoDataSuccess(position)
                        } else {
                            mRootView.updateTodoDataFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.updateTodoDataFailed(HttpUtils.getErrorText(t))
                    }
                })

    }

    fun updateTodo(id: Int, position: Int) {
        mModel.updateTodoData(id, 1) //1完成
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))
                .doOnSubscribe {
                    mRootView.showLoading() //显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mRootView.hideLoading() //隐藏加载框
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            mRootView.updateTodoDataSuccess(position)
                        } else {
                            mRootView.updateTodoDataFailed(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.updateTodoDataFailed(HttpUtils.getErrorText(t))
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
