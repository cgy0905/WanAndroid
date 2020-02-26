package com.cgy.wandroid.mvp.contract

import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.UserInfoResponse
import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable


/**
 * @author: cgy
 * @description:
 * 2019/9/16 17:21
 */
interface LoginContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun onSuccess(userInfo: UserInfoResponse)
        fun showProgress()
        fun closeProgress()
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun login(username:String,password:String): Observable<ApiResponse<UserInfoResponse>>
        fun register(username : String, password : String, confirmPwd :String) : Observable<ApiResponse<Any>>
    }
}