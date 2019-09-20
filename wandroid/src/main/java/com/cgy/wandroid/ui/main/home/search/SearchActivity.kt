package com.cgy.wandroid.ui.main.home.search

import android.content.Intent
import android.os.Bundle

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerSearchComponent
import com.cgy.wandroid.di.module.SearchModule
import com.cgy.wandroid.mvp.contract.SearchContract
import com.cgy.wandroid.mvp.presenter.SearchPresenter

import com.cgy.wandroid.R

/**
 * @author: cgy
 * @description:
 * @date: 2019/09/20 14:44
 */
class SearchActivity : BaseActivity<SearchPresenter>(), SearchContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchModule(SearchModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_search //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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
