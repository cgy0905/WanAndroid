package com.cgy.wandroid.mvp.contract

import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.TodoResponse
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable


interface TodoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestDataSuccess(articles : ApiPagerResponse<MutableList<TodoResponse>>)
        fun requestDataFailed(errorMsg : String)
        fun updateTodoDataSuccess(position : Int)
        fun deleteTodoDataSuccess(position: Int)
        fun updateTodoDataFailed(errorMsg: String)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getTodoData(pageNo : Int) : Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>
        fun updateTodoData(id : Int, status : Int) : Observable<ApiResponse<Any>>
        fun deleteTodoData(id: Int) : Observable<ApiResponse<Any>>
    }

}
