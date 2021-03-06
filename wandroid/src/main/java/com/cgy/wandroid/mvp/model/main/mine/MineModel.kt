package com.cgy.wandroid.mvp.model.main.mine

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.MineContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.IntegralResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/20/2019 11:33
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class MineModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), MineContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    override fun getIntegral(): Observable<ApiResponse<IntegralResponse>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getIntegral())
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }



    override fun onDestroy() {
        super.onDestroy()
    }
}
