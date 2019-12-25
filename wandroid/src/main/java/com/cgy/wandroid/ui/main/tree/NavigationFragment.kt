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

import com.cgy.wandroid.di.component.DaggerNavigationComponent
import com.cgy.wandroid.di.module.NavigationModule
import com.cgy.wandroid.mvp.contract.NavigationContract
import com.cgy.wandroid.mvp.presenter.NavigationPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.model.entity.NavigationResponse
import com.cgy.wandroid.ui.main.tree.adapter.NavigationAdapter
import com.cgy.wandroid.ui.web.WebViewActivity
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.util.SpaceItemDecoration
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.layout_footer_loadmore.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description: 导航
 * @date: 2019/12/17 11:27
 */
class NavigationFragment : BaseFragment<NavigationPresenter>(), NavigationContract.View {

    lateinit var loadSir : LoadService<Any>
    lateinit var adapter : NavigationAdapter
    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerNavigationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .navigationModule(NavigationModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadsir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.swipe_refresh_layout)) {
            loadSir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            mPresenter?.getNavigationData()
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
            //设置刷新监听回调
            setOnRefreshListener {
                mPresenter?.getNavigationData()
            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前recyclerView 最后一个视图位置的索引大于等于40,则迅速返回顶部,否则带有滚动动画效果返回到顶部
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
            //监听recyclerView滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
        adapter  = NavigationAdapter(mutableListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            //设置点击tag的回调
            setOnTagClickListener(object : NavigationAdapter.OnTagClickListener{
                override fun onClick(position: Int, childPosition: Int) {
                    // position = 点击了第几个item, childPosition 点击的第几个tag
                    launchActivity(Intent(_mActivity, WebViewActivity::class.java).apply {
                      putExtras(Bundle().also {
                          it.putSerializable("data", adapter.data[position].articles[childPosition])
                          it.putString("tag", this@NavigationFragment::class.java.simpleName)
                          it.putInt("tab", position)
                          it.putInt("position", childPosition)
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
        mPresenter?.getNavigationData()//请求数据
    }

    /**
     * 获取导航数据回调
     */
    override fun getNavigationDataSuccess(data: MutableList<NavigationResponse>) {
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


    @Subscribe
    fun freshLogin(event : LoginFreshEvent) {
        //因为导航界面没有需要显示是否收藏,所以只要改动数据就好了
        //如果是登录了,当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in adapter.data) {
                    for (article in item.articles) {
                        if (it.toInt() == article.id) {
                            article.collect = true
                        }
                    }
                }
            }
        } else {
            //退出了,把所有的收藏全部变为未收藏
            for (item in adapter.data) {
                for (article in item.articles) {
                    article.collect = false
                }
            }
        }
    }

    /**
     * 在详情中收藏时,接收到EventBus
     */
    @Subscribe
    fun collectChange(event : CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                adapter.data.forEach {
                    for (index in it.articles.indices) {
                        if (it.articles[index].id == event.id) {
                            //因为导航界面没有需要显示是否收藏,所以只要改动数据就好了
                            it.articles[index].collect = event.collect
                            break
                        }
                    }
                }
            }
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
