package com.cgy.wandroid.ui.main.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerProjectComponent
import com.cgy.wandroid.di.module.ProjectModule
import com.cgy.wandroid.mvp.contract.ProjectContract
import com.cgy.wandroid.mvp.presenter.ProjectPresenter

import com.cgy.wandroid.R


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/19/2019 15:19
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
 * @date: 2019/09/19 15:19
 */
class ProjectFragment : BaseFragment<ProjectPresenter>(), ProjectContract.View {
    companion object {
        fun newInstance(): ProjectFragment {
            val fragment = ProjectFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .projectModule(ProjectModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_project, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
