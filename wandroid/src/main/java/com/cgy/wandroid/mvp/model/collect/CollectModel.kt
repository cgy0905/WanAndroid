package com.cgy.wandroid.mvp.model.collect

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.CollectContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.CollectResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@FragmentScope
class CollectModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), CollectContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getCollectData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<CollectResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getCollectData(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun unCollectList(id: Int, originId: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .uncollectList(id, originId))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}
