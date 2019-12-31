package com.cgy.wandroid.mvp.contract

import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.IntegralHistoryResponse
import com.cgy.wandroid.mvp.model.entity.IntegralResponse
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable


interface IntegralContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestDataSuccess(articles : ApiPagerResponse<MutableList<IntegralResponse>>)
        fun requestHistoryDataSuccess(articles: ApiPagerResponse<MutableList<IntegralHistoryResponse>>)
        fun requestDataFailed(errorMsg: String)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getIntegralData(pageNo : Int) : Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>>
        fun getIntegralHistoryData(pageNo:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>>
    }

}
