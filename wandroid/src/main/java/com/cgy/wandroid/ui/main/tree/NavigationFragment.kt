package com.cgy.wandroid.ui.main.tree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerNavigationComponent
import com.cgy.wandroid.di.module.NavigationModule
import com.cgy.wandroid.mvp.contract.NavigationContract
import com.cgy.wandroid.mvp.presenter.NavigationPresenter

import com.cgy.wandroid.R


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/17 11:27
 */
class NavigationFragment : BaseFragment<NavigationPresenter>(), NavigationContract.View {
    companion object {
        fun newInstance(): NavigationFragment {
            val fragment = NavigationFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerNavigationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .navigationModule(NavigationModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
