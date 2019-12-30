package com.cgy.wandroid.mvp.contract

import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.CollectResponse
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable


interface CollectContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<CollectResponse>>)
        fun requestDataFailed(errorMsg : String)
        fun unCollect(position : Int)
        fun unCollectFailed(position: Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        //根据分类id获取项目数据
        fun getCollectData(pageNo : Int) : Observable<ApiResponse<ApiPagerResponse<MutableList<CollectResponse>>>>
        fun unCollectList(id : Int, originId : Int) : Observable<ApiResponse<Any>>
    }

}
