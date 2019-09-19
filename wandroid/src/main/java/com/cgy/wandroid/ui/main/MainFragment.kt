
package com.cgy.wandroid.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerMainComponent
import com.cgy.wandroid.di.module.MainModule
import com.cgy.wandroid.mvp.contract.MainContract
import com.cgy.wandroid.mvp.presenter.MainPresenter

import com.cgy.wandroid.ui.main.home.HomeFragment
import com.jess.arms.di.component.AppComponent
import me.yokeyword.fragmentation.SupportFragment


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


    }


}
