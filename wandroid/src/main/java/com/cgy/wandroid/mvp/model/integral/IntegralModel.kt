package com.cgy.wandroid.mvp.model.integral

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.IntegralContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.IntegralHistoryResponse
import com.cgy.wandroid.mvp.model.entity.IntegralResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@ActivityScope
class IntegralModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), IntegralContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    override fun getIntegralData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getIntegralRank(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun getIntegralHistoryData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getIntegralHistory(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
