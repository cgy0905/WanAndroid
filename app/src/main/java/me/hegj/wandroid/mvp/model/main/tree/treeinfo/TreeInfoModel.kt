package me.hegj.wandroid.mvp.model.main.tree.treeinfo

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.contract.main.tree.treeinfo.TreeInfoContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.ArticleResponse
import javax.inject.Inject

/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/23/2019 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class TreeInfoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), TreeInfoContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getTreeInfoDatas(pageNo: Int,cid:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getAritrilDataByTree(pageNo,cid))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }


    //取消收藏
    override fun unCollect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .uncollect(id))
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
