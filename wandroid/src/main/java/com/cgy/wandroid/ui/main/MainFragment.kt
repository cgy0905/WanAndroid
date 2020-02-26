
package com.cgy.wandroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerMainComponent
import com.cgy.wandroid.di.module.MainModule
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.contract.MainContract
import com.cgy.wandroid.mvp.presenter.MainPresenter

import com.cgy.wandroid.ui.main.home.HomeFragment
import com.cgy.wandroid.ui.main.mine.MineFragment
import com.cgy.wandroid.ui.main.project.ProjectFragment
import com.cgy.wandroid.ui.main.pub.PublicFragment
import com.cgy.wandroid.ui.main.tree.TreeFragment
import com.cgy.wandroid.util.SettingUtil
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.fragment_main.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.Subscribe


/**
 * 主界面Fragment
 */
class MainFragment : BaseFragment<MainPresenter>(), MainContract.View {
    private val first  = 0
    private val two    = 1
    private val three  = 2
    private val four   = 3
    private val five   = 4
    private val mFragments = arrayOfNulls<SupportFragment>(5)


    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(MainModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val homeFragment = findChildFragment(HomeFragment::class.java)
        if (homeFragment == null) {
            mFragments[first] = HomeFragment.newInstance()//主页
            mFragments[two] = ProjectFragment.newInstance()//项目
            mFragments[three] = TreeFragment.newInstance()//体系
            mFragments[four] = PublicFragment.newInstance()//公众号
            mFragments[five] = MineFragment.newInstance()//我的

            loadMultipleRootFragment(R.id.fl_content, first, mFragments[first], mFragments[two],
                    mFragments[three], mFragments[four], mFragments[five])
        } else {
            mFragments[first] = homeFragment
            mFragments[two] = findChildFragment(ProjectFragment::class.java)
            mFragments[three] = findChildFragment(TreeFragment::class.java)
            mFragments[four]  = findChildFragment(PublicFragment::class.java)
            mFragments[five]  = findChildFragment(MineFragment::class.java)
        }
        main_tab.run {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
            itemIconTintList = SettingUtil.getOneColorStateList(_mActivity)
            itemTextColor = SettingUtil.getColorStateList(_mActivity)
            setIconSize(20F, 20F)
            setTextSize(12F)
            setOnNavigationItemSelectedListener {
                when (it.itemId){
                    R.id.menu_main -> showHideFragment(mFragments[first])
                    R.id.menu_project -> showHideFragment(mFragments[two])
                    R.id.menu_system -> showHideFragment(mFragments[three])
                    R.id.menu_public -> showHideFragment(mFragments[four])
                    R.id.menu_me -> showHideFragment(mFragments[five])
                }
                true
            }
        }

    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        main_tab?.run {
            itemIconTintList = SettingUtil.getColorStateList(_mActivity)
            itemTextColor = SettingUtil.getColorStateList(_mActivity)
        }
    }


}
