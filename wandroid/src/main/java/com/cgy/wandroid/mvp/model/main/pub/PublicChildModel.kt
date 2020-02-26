package com.cgy.wandroid.mvp.model.main.pub

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.PublicChildContract
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
class PublicChildModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), PublicChildContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getPublicData(pageNo: Int,cid:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getPublicNewData(pageNo,cid))
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
    //收藏
    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collect(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
