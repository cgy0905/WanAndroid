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
import com.cgy.wandroid.ui.main.home.adapter.ArticleAdapter
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/17 16:32
 */
class TreeInfoFragment : BaseFragment<TreeInfoPresenter>(), TreeInfoContract.View {
    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: ArticleAdapter
    private var initPageNo = 0 //注意,体系页码从0开始的
    private var pageNo: Int = initPageNo //注意,体系页码从0开始的
    private var cid: Int = 0
    private var footView: DefineLoadMoreView? = null
    companion object {
        fun newInstance(): TreeInfoFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            val fragment = TreeInfoFragment()
            fragment.arguments = args
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
