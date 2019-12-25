package com.cgy.wandroid.mvp.model.web

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.WebViewContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.CollectUrlResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/20/2019 15:41
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class WebViewModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), WebViewContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collect(id))
                .flatMap{ apiResponseObservable ->
            apiResponseObservable
        }
    }

    override fun collectUrl(name: String, link: String): Observable<ApiResponse<CollectUrlResponse>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collectUrl(name, link))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    //取消收藏
    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .unCollect(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    //取消收藏
    override fun unCollectList(id: Int, originId: Int): Observable<ApiResponse<Any>>{
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .uncollectList(id,originId))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    //取消收藏网址
    override fun unCollectUrl(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .deletetool(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}
