package com.cgy.wandroid.ui.main.home.search

import android.content.Intent
import android.os.Bundle

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerSearchResultComponent
import com.cgy.wandroid.di.module.SearchResultModule
import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.cgy.wandroid.mvp.presenter.SearchResultPresenter

import com.cgy.wandroid.ui.main.home.search.R


/**
 * @author: cgy
 * @description: 
 * @date: 2019/12/16 13:50
 */
class SearchResultActivity : BaseActivity<SearchResultPresenter>() , SearchResultContract.View {

    override fun setupActivityComponent(appComponent:AppComponent) {
        DaggerSearchResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchResultModule(SearchResultModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState:Bundle?):Int {
              return R.layout.activity_search_result //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }



    override fun initData(savedInstanceState:Bundle?) {

    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message:String) {
        ArmsUtils.snackbarText(message)
    }
    override fun launchActivity(intent:Intent) {
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }
}
