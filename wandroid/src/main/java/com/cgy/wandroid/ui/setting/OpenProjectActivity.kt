package com.cgy.wandroid.ui.setting

import android.content.Intent
import android.os.Bundle

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerOpenProjectComponent
import com.cgy.wandroid.di.module.OpenProjectModule
import com.cgy.wandroid.mvp.contract.OpenProjectContract
import com.cgy.wandroid.mvp.presenter.OpenProjectPresenter

import com.cgy.wandroid.R


/**
 * @author: cgy
 * @description:
 * @date: 2020/01/03 11:08
 */
class OpenProjectActivity : BaseActivity<OpenProjectPresenter>(), OpenProjectContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerOpenProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .openProjectModule(OpenProjectModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_open_project //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {

    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }
}
