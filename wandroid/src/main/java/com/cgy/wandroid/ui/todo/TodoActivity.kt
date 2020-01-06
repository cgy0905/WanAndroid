package com.cgy.wandroid.ui.todo

import android.content.Intent
import android.os.Bundle

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerTodoComponent
import com.cgy.wandroid.di.module.TodoModule
import com.cgy.wandroid.mvp.contract.TodoContract
import com.cgy.wandroid.mvp.presenter.TodoPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.ui.adapter.TodoAdapter
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * @author: cgy
 * @description: 任务清单
 * @date: 2019/12/25 15:53
 */
class TodoActivity : BaseActivity<TodoPresenter>(), TodoContract.View {
    lateinit var loadSir : LoadService<Any>
    lateinit var adapter : TodoAdapter
    private var initPageNo = 1
    private var pageNo : Int = initPageNo //当前页码
    private var footView : DefineLoadMoreView? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerTodoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .todoModule(TodoModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_todo //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "待办清单"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }

        //绑定loadSir
        loadSir = LoadSir.getDefault().register(swipe_refresh_layout) {
            //界面加载失败,或者没有数据时,点击重试的监听
            loadSir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getTodoData(pageNo)
        }.apply {
            SettingUtil.setLoadingColor(this@TodoActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter
        adapter = TodoAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(this@TodoActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@TodoActivity))
            } else {
                closeLoadAnimation()
            }
            setOnItemClickListener { adapter, view, position ->
                launchActivity(Intent(this@TodoActivity, AddTodoActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", this@TodoActivity.adapter.data[position])
                    })
                })
            }
        }
    }


}
