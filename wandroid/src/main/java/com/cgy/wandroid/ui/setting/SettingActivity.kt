package com.cgy.wandroid.ui.setting

import android.os.Bundle
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.event.SettingChangeEvent
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import kotlinx.android.synthetic.main.include_toolbar.*
import org.greenrobot.eventbus.Subscribe

class SettingActivity : BaseActivity<IPresenter>() {

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
       return R.layout.activity_setting
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "设置"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fl_setting, GeneralPreferenceFragment())
                .commit()
    }

    @Subscribe
    fun settingEvent(event : SettingChangeEvent) {
        initStatusBar()
    }

}
