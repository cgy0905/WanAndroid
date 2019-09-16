package com.cgy.wandroid.ui.login

import android.os.Bundle
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.di.component.DaggerLoginComponent
import com.cgy.wandroid.di.module.LoginModule
import com.cgy.wandroid.mvp.contract.LoginContract
import com.cgy.wandroid.mvp.model.entity.UserInfoResponse
import com.cgy.wandroid.mvp.presenter.LoginPresenter
import com.jess.arms.di.component.AppComponent

/**
 * @author: cgy
 * @description:
 * 2019/9/16 17:20
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {
    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .build()
                .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun onSuccess(userInfo: UserInfoResponse) {

    }
}