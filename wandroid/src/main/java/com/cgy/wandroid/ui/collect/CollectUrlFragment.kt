package com.cgy.wandroid.ui.collect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.cgy.wandroid.base.BaseFragment
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.component.DaggerCollectUrlComponent
import com.cgy.wandroid.di.module.CollectUrlModule
import com.cgy.wandroid.mvp.contract.CollectUrlContract
import com.cgy.wandroid.mvp.presenter.CollectUrlPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.mvp.model.entity.CollectUrlResponse
import com.cgy.wandroid.ui.adapter.CollectUrlAdapter
import com.cgy.wandroid.ui.web.WebViewActivity
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.util.SpaceItemDecoration
import com.cgy.wandroid.weight.CollectView
import com.cgy.wandroid.weight.loadCallback.EmptyCallback
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.chad.library.adapter.base.BaseViewHolder
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/26 09:37
 */
class CollectUrlFragment : BaseFragment<CollectUrlPresenter>(), CollectUrlContract.View {


    lateinit var loadSir: LoadService<Any>
    lateinit var adapter: CollectUrlAdapter

    companion object {
        fun newInstance(): CollectUrlFragment {
            return CollectUrlFragment()
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerCollectUrlComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .collectUrlModule(CollectUrlModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.swipe_refresh_layout)) {
            loadSir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            mPresenter?.getCollectUrlData()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return rootView
    }

    override fun initData(savedInstanceState: Bundle?) {
        //初始化swipeRefreshLayout
        swipe_refresh_layout.run {
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            //设置刷新监听回调
            setOnRefreshListener {
                mPresenter?.getCollectUrlData()
            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前recyclerView 最后一个视图位置的索引大于等于40, 则迅速返回顶部,否则带有滚动动画效果返回到顶部
                if (layoutManager.findFirstVisibleItemPosition() >= 40) {
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
                    float_action_btn.visibility = View.INVISIBLE
                }
            })
        }
        //初始化adapter
        adapter = CollectUrlAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            //点击爱心收藏
            setOnCollectViewClickListener(object : CollectUrlAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    mPresenter?.unCollect(adapter.data[position].id, position)
                }

            })
            //点击了整行
            setOnItemClickListener { _, view, position ->
                val intent = Intent(_mActivity, WebViewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("collectUrl", adapter.data[position])
                    putString("tag", this@CollectUrlFragment::class.java.simpleName)
                    putInt("position", position)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        swipe_recycler_view.adapter = adapter//设置适配器
        loadSir.showCallback(LoadingCallback::class.java)//设置加载中
        mPresenter?.getCollectUrlData()//请求数据
    }

    override fun requestDataUrlSuccess(apiPagerResponse: MutableList<CollectUrlResponse>) {
        swipe_refresh_layout.isRefreshing = false
        if (apiPagerResponse.size == 0) {
            //如果没有数据,页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else {
            //有数据
            loadSir.showSuccess()
            adapter.setNewData(apiPagerResponse)
        }
    }

    override fun requestDataFailed(errorMsg: String) {
        swipe_refresh_layout.isRefreshing = false
        loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
            //设置错误页文字提示
            view.findViewById<TextView>(R.id.error_text).text = errorMsg
            loadSir.showCallback(ErrorCallback::class.java)
        }
    }

    override fun unCollect(position: Int) {
        //当前收藏数据大于1条的时候,直接删除
        if (adapter.data.size > 1) {
            adapter.remove(position)
        } else {
            //小于等于1条时,不要删除,直接给界面设置空数据
            loadSir.showCallback(EmptyCallback::class.java)
        }
    }

    override fun unCollectFailed(position: Int) {
        adapter.notifyItemChanged(position)
    }

    /**
     * 在详情中收藏时,接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        swipe_refresh_layout.isRefreshing = true
        mPresenter?.getCollectUrlData()
    }


}
