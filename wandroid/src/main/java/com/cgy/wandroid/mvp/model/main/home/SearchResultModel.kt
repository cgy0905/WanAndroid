package com.cgy.wandroid.mvp.model.main.home

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@ActivityScope
class SearchResultModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), SearchResultContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    override fun getArticleList(pageNo: Int, searchKey: String): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getSearchDataByKey(pageNo, searchKey))
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
