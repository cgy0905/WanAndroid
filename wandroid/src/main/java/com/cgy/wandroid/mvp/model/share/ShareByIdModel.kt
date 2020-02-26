package com.cgy.wandroid.mvp.model.share

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.ShareByIdContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ShareResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@ActivityScope
class ShareByIdModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), ShareByIdContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    override fun getShareData(pageNo: Int,id:Int): Observable<ApiResponse<ShareResponse>> {
        return mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getShareByIdData(pageNo,id)
    }

    //取消收藏
    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .unCollect(id)
    }
    //收藏
    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collect(id)
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}
