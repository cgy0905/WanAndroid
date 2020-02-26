package com.cgy.wandroid.mvp.model.main.tree

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.TreeInfoContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@FragmentScope
class TreeInfoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), TreeInfoContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getTreeInfoData(pageNo: Int, cid: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getAritrilDataByTree(pageNo, cid))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    //收藏
    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collect(id))
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

    override fun onDestroy() {
        super.onDestroy()
    }
}
