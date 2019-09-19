<<<<<<< HEAD
package com.cgy.wandroid.ui.main
=======
package com.cgy.wandroid.ui
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4

import android.os.Bundle
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.event.SettingChangeEvent
<<<<<<< HEAD
=======
import com.cgy.wandroid.ui.fragment.MainFragment
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
import com.cgy.wandroid.util.ShowUtil
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
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
