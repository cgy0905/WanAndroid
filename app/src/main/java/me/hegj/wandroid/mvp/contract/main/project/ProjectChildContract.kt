package me.hegj.wandroid.mvp.contract.main.project

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.ArticleResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/07/2019 17:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
interface ProjectChildContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView{
        fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>)
        fun requestDataFailed(errorMsg: String)
        fun  collect(collected:Boolean,position:Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel{
        //根据分类id获取项目数据
        fun getProjects(pageNo:Int,cid:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>
        //获取最新项目数据
        fun getNewProjects(pageNo:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>
        fun collect(id:Int):Observable<ApiResponse<Any>>
        fun uncollect(id:Int):Observable<ApiResponse<Any>>
    }

}
