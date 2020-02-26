
package com.cgy.wandroid.ui.main

import android.os.Bundle
import com.cgy.wandroid.BuildConfig
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.util.ShowUtil
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.tencent.bugly.beta.Beta
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator


/**
 * @author: cgy
 * @description: 主界面
 * @date : 2019/9/19 14.04
 */
class MainActivity : BaseActivity<IPresenter>() {

    override fun setupActivityComponent(appComponent: AppComponent) {
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.fl_content, MainFragment.newInstance())
        }
        //进入首页检查更新
        Beta.checkUpgrade(false, true)

        if(BuildConfig.APPLICATION_ID != "com.cgy.wandroid"&&BuildConfig.BUGLY_KEY =="329337f47c"){
            showMessage("请更换Bugly Key！防止产生的错误信息反馈到作者账号上，具体请查看app模块中的 build.gradle文件，修改BUGLY_KEY字段值为自己在Bugly官网申请的Key")
        }
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        //设置横向(和android4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    /**
     * 启动一个其他的Fragment
     */
    fun startAnotherFragment(targetFragment: SupportFragment) {
        start(targetFragment)
    }

    fun settingEvent(event: SettingChangeEvent) {
        initStatusBar()
    }

    var exitTime : Long = 0

    override fun onBackPressedSupport() {
        if (System.currentTimeMillis() - this.exitTime > 2000L) {
            ShowUtil.showToast(this, "再按一次退出程序")
            this.exitTime = System.currentTimeMillis()
            return
        } else {
            super.onBackPressedSupport()
        }

    }


}
