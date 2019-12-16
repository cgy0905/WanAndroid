package com.cgy.wandroid.mvp.model.main.project

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.ProjectChildContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
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
 * Created by MVPArmsTemplate on 10/30/2019 11:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class ProjectChildModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), ProjectChildContract.Model {


    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getProjects(pageNo: Int, cid: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getProjecDataByType(pageNo, cid))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun getNewProjects(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getProjecNewData(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    //取消收藏
    override fun uncollect(id: Int): Observable<ApiResponse<Any>> {
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
