package com.cgy.wandroid.ui.main.tree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.component.DaggerTreeComponent
import com.cgy.wandroid.di.module.TreeModule
import com.cgy.wandroid.mvp.contract.TreeContract
import com.cgy.wandroid.mvp.presenter.TreePresenter

import com.cgy.wandroid.R

/**
 * @author: cgy
 * @description:
 * @date: 2019/09/20 10:01
 */
class TreeFragment : BaseFragment<TreePresenter>(), TreeContract.View {
    companion object {
        fun newInstance(): TreeFragment {
            val fragment = TreeFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerTreeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .treeModule(TreeModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_tree, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
