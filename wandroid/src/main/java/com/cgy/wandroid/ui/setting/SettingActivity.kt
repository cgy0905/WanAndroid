package com.cgy.wandroid.ui.setting

import android.os.Bundle
import com.cgy.wandroid.R
import com.jess.arms.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter

class SettingActivity : BaseActivity<IPresenter>() {
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
       return R.layout.activity_setting
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}
