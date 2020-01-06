package com.cgy.wandroid.ui.todo

import android.content.Intent
import android.os.Bundle

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerAddTodoComponent
import com.cgy.wandroid.di.module.AddTodoModule
import com.cgy.wandroid.mvp.contract.AddTodoContract
import com.cgy.wandroid.mvp.presenter.AddTodoPresenter

import com.cgy.wandroid.R


/**
 * @author: cgy
 * @description:
 * @date: 2020/01/06 13:54
 */
class AddTodoActivity : BaseActivity<AddTodoPresenter>(), AddTodoContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerAddTodoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addTodoModule(AddTodoModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_add_todo //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {

    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }
}
