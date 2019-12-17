package com.cgy.wandroid.ui.main.tree.treeinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.component.DaggerTreeInfoComponent
import com.cgy.wandroid.di.module.TreeInfoModule
import com.cgy.wandroid.mvp.contract.TreeInfoContract
import com.cgy.wandroid.mvp.presenter.TreeInfoPresenter

import com.cgy.wandroid.R


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/17 16:32
 */
class TreeInfoFragment : BaseFragment<TreeInfoPresenter>(), TreeInfoContract.View {
    companion object {
        fun newInstance(): TreeInfoFragment {
            val fragment = TreeInfoFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerTreeInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .treeInfoModule(TreeInfoModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tree_info, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
