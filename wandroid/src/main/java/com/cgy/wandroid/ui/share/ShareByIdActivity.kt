package com.cgy.wandroid.ui.share


import android.os.Bundle
import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.component.DaggerShareByIdComponent
import com.cgy.wandroid.di.module.ShareByIdModule
import com.cgy.wandroid.mvp.contract.ShareByIdContract
import com.cgy.wandroid.mvp.presenter.ShareByIdPresenter

import com.cgy.wandroid.R


/**
 * @author: cgy
 * @description:
 * @date: 2020/01/08 17:42
 */
class ShareByIdActivity : BaseActivity<ShareByIdPresenter>(), ShareByIdContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerShareByIdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .shareByIdModule(ShareByIdModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_share_by_id //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {

    }

}
