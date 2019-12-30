package com.cgy.wandroid.mvp.model.collect

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.CollectUrlContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.CollectUrlResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@FragmentScope
class CollectUrlModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), CollectUrlContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getCollectUrlData(): Observable<ApiResponse<MutableList<CollectUrlResponse>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getCollectUrlData())
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun unCollectList(id: Int): Observable<ApiResponse<Any>> {
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
