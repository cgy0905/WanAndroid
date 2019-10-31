package com.cgy.wandroid.ui.main.project.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerProjectChildComponent
import com.cgy.wandroid.di.module.ProjectChildModule
import com.cgy.wandroid.mvp.contract.ProjectChildContract
import com.cgy.wandroid.mvp.presenter.ProjectChildPresenter

import com.cgy.wandroid.ui.main.project.R


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/30/2019 11:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
/**
 * @author: cgy
 * @description:
 * @date: 2019/10/30 11:22
 */
class ProjectChildFragment : BaseFragment<ProjectChildPresenter>(), ProjectChildContract.View {
    companion object {
        fun newInstance(): ProjectChildFragment {
            val fragment = ProjectChildFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectChildComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .projectChildModule(ProjectChildModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_project_child, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
