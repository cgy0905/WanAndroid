package com.cgy.wandroid.ui.integral

import android.content.Intent
import android.os.Bundle

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerIntegralComponent
import com.cgy.wandroid.di.module.IntegralModule
import com.cgy.wandroid.mvp.contract.IntegralContract
import com.cgy.wandroid.mvp.presenter.IntegralPresenter

import com.cgy.wandroid.R


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/25 16:03
 */
class IntegralActivity : BaseActivity<IntegralPresenter>(), IntegralContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerIntegralComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .integralModule(IntegralModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_integral //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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
