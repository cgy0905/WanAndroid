package com.cgy.wandroid.mvp.contract

import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.CollectUrlResponse
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable


interface CollectUrlContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestDataUrlSuccess(apiPagerResponse: MutableList<CollectUrlResponse>)
        fun requestDataFailed(errorMsg : String)
        fun unCollect(position : Int)
        fun unCollectFailed(position: Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getCollectUrlData() : Observable<ApiResponse<MutableList<CollectUrlResponse>>>
        fun unCollectList(id : Int) : Observable<ApiResponse<Any>>
    }

}
