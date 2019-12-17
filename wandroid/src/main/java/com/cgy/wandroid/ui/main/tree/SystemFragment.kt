package com.cgy.wandroid.ui.main.tree

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerSystemComponent
import com.cgy.wandroid.di.module.SystemModule
import com.cgy.wandroid.mvp.contract.SystemContract
import com.cgy.wandroid.mvp.presenter.SystemPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.model.entity.SystemResponse
import com.cgy.wandroid.ui.main.tree.adapter.SystemAdapter
import com.cgy.wandroid.ui.main.tree.treeinfo.TreeInfoActivity
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.util.SpaceItemDecoration
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description: 体系
 * @date: 2019/12/17 11:25
 */
class SystemFragment : BaseFragment<SystemPresenter>(), SystemContract.View {
    lateinit var loadSir : LoadService<Any>
    lateinit var adapter : SystemAdapter
    companion object {
        fun newInstance(): SystemFragment {
            val fragment = SystemFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerSystemComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .systemModule(SystemModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.swipe_refresh_layout)) {
            //点击重试时请求
            mPresenter?.getSystemData()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return rootView
    }

    override fun initData(savedInstanceState: Bundle?) {
        //初始化swipeRefreshLayout
        swipe_refresh_layout.run {
            //设置颜色
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            //设置刷新监听
            setOnRefreshListener {
                mPresenter?.getSystemData()
            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    swipe_recycler_view.scrollToPosition(0)//没有动画迅速返回到顶部(极快)
                } else {
                    swipe_recycler_view.smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
                }
            }
        }
        //初始化recyclerView
        swipe_recycler_view.run {
            layoutManager = LinearLayoutManager(_mActivity)
            setHasFixedSize(true)
            //设置item的行间距
            addItemDecoration(SpaceItemDecoration(0, UIUtil.dip2px(_mActivity, 8.0)))
            //监听recyclerView滑动到顶部的时候,需要把向上返回顶部的按钮隐藏
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("RestrictedApi")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(-1)) {
                        float_action_btn.visibility = View.INVISIBLE
                    }
                }
            })
        }
        //初始化适配器
        adapter = SystemAdapter(mutableListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            setOnItemClickListener { _, view, position ->
                launchActivity(Intent(_mActivity, TreeInfoActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", adapter.data[position])
                        putInt("position", 0)
                    })
                })
            }
            //设置点击tag的回调
            setOnTagClickListener(object : SystemAdapter.OnTagClickListener {
                override fun onClick(position: Int, childPosition: Int) {
                    // position = 点击了第几个item, childPosition 点击的第几个tag
                    launchActivity(Intent(_mActivity, TreeInfoActivity::class.java).apply {
                        putExtras(Bundle().apply {
                            putSerializable("data", adapter.data[position])
                            putInt("position", childPosition)
                        })
                    })
                }

            })
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        swipe_recycler_view.adapter = adapter//设置适配器
        loadSir.showCallback(LoadingCallback::class.java)//设置加载中
        mPresenter?.getSystemData()//请求数据
    }

    override fun getSystemDataSuccess(data: MutableList<SystemResponse>) {
        float_action_btn.visibility = View.INVISIBLE
        if (data.size == 0) {
            //集合大小为0 说明肯定是第一次请求数据并且请求失败了,因为只要请求成功过一次就会有缓存数据
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            swipe_refresh_layout.isRefreshing = false
            loadSir.showCallback(SuccessCallback::class.java)
            adapter.setNewData(data)
        }
    }

    /**
     * 接收到event时,重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event : SettingChangeEvent) {
        float_action_btn.backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
        SettingUtil.setLoadingColor(_mActivity, loadSir)
        swipe_refresh_layout.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        adapter.run {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
        }
    }


}
