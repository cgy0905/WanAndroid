package com.cgy.wandroid.ui.main.home


import android.os.Bundle


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent


import com.cgy.wandroid.di.component.DaggerHomeComponent
import com.cgy.wandroid.di.module.HomeModule
import com.cgy.wandroid.mvp.contract.HomeContract
import com.cgy.wandroid.mvp.presenter.HomePresenter

import com.cgy.wandroid.R


/**
 * @author：cgy
 * @description：
 * @date：09/19/2019 15:00
 */
class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.View {
    companion object {
        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(HomeModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
