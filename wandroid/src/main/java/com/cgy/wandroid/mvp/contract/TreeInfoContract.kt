package com.cgy.wandroid.mvp.contract

import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable


interface TreeInfoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>)
        fun requestDataFailed(errorMsg : String)
        fun collect(collected : Boolean, position : Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getTreeInfoData(pageNo : Int, cid : Int) : Observable<ApiResponse<ApiPagerResponse<MutableList<ArticleResponse>>>>
        fun collect(id : Int) : Observable<ApiResponse<Any>>
        fun unCollect(id : Int) : Observable<ApiResponse<Any>>
    }

}
