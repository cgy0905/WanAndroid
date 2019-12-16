package com.cgy.wandroid.ui.main.pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerpublicChildComponent
import com.cgy.wandroid.di.module.publicChildModule
import com.cgy.wandroid.mvp.contract.publicChildContract
import com.cgy.wandroid.mvp.presenter.publicChildPresenter

import com.cgy.wandroid.ui.main.pub.R


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/12 15:37
 */
class publicChildFragment : BaseFragment<publicChildPresenter>(), publicChildContract.View {
    companion object {
        fun newInstance(): publicChildFragment {
            val fragment = publicChildFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerpublicChildComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .publicChildModule(publicChildModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_public_child, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
