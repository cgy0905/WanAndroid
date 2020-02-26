package com.cgy.wandroid.mvp.model.main.tree

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.NavigationContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.NavigationResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@FragmentScope
class NavigationModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), NavigationContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    override fun getNavigationData(): Observable<ApiResponse<MutableList<NavigationResponse>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getNavigationData())
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}
