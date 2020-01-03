package com.cgy.wandroid.ui.main.tree.treeinfo

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

import com.cgy.wandroid.di.component.DaggerTreeInfoComponent
import com.cgy.wandroid.di.module.TreeInfoModule
import com.cgy.wandroid.mvp.contract.TreeInfoContract
import com.cgy.wandroid.mvp.presenter.TreeInfoPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.ui.adapter.ArticleAdapter
import com.cgy.wandroid.ui.web.WebViewActivity
import com.cgy.wandroid.util.RecyclerViewUtils
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.CollectView
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.cgy.wandroid.weight.loadCallback.EmptyCallback
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.chad.library.adapter.base.BaseViewHolder
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.hegj.wandroid.app.event.CollectEvent
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description:
 * @date: 2019/12/17 16:32
 */
class TreeInfoFragment : BaseFragment<TreeInfoPresenter>(), TreeInfoContract.View {
    lateinit var loadSir: LoadService<Any>
    lateinit var adapter: ArticleAdapter
    private var initPageNo = 0 //注意,体系页码从0开始的
    private var pageNo: Int = initPageNo //注意,体系页码从0开始的
    private var cid: Int = 0
    private var footView: DefineLoadMoreView? = null
    companion object {
        fun newInstance(cid : Int): TreeInfoFragment {
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
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.swipe_refresh_layout)) {
            pageNo = initPageNo
            mPresenter?.getTreeInfoDataByType(pageNo, cid = cid)
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return rootView
    }

    override fun initData(savedInstanceState: Bundle?) {
        cid = arguments?.getInt("cid") ?: 0
        //初始化swipeRefreshLayout
        swipe_refresh_layout.run {
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            setOnRefreshListener {
                pageNo = initPageNo //刷新
                mPresenter?.getTreeInfoDataByType(pageNo, cid = cid)
            }
        }
        //初始化adapter
        adapter = ArticleAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            //点击爱心收藏执行操作
            setOnCollectViewClickListener(object : ArticleAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    if (v.isChecked) {
                        mPresenter?.unCollect(adapter.data[position].id, position)
                    } else {
                        mPresenter?.collect(adapter.data[position].id, position)
                    }
                }

            })
            //点击了整行
            setOnItemClickListener { _, view, position ->
                val intent = Intent(_mActivity, WebViewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("data", adapter.data[position])
                    putString("tag", this@TreeInfoFragment::class.java.simpleName)
                    putInt("position", position)
                    putInt("tab", cid)
                }
                intent.putExtras(bundle)
                startActivity(intent)
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
        footView = RecyclerViewUtils().initRecyclerView(_mActivity, swipe_recycler_view, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getTreeInfoDataByType(pageNo, cid = cid)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        }
        swipe_recycler_view.run {
            ////监听recyclerView滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
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
    }

    /**
     * 懒加载,只有该fragment获得视图时才会调用
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        loadSir.showCallback(LoadingCallback::class.java)//默认设置界面加载中
        swipe_recycler_view.adapter = adapter
        mPresenter?.getTreeInfoDataByType(pageNo, cid = cid)
    }

    @SuppressLint("RestrictedApi")
    override fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<ArticleResponse>>) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo && apiPagerResponse.datas.size == 0) {
            //如果是第一页,并且没有数据,页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新的话,floatButton就隐藏
            float_action_btn.visibility = View.INVISIBLE
            adapter.setNewData(apiPagerResponse.datas)
        } else {
            //不是第一页 且有数据
            loadSir.showSuccess()
            adapter.addData(apiPagerResponse.datas)
        }
        pageNo++
        if (apiPagerResponse.pageCount >= pageNo) {
            //如果总条数大于等于当前页数时,还有更多数据
            swipe_recycler_view.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            swipe_recycler_view.postDelayed({
                swipe_recycler_view.loadMoreFinish(false, false)
            }, 200)
        }

    }

    override fun requestDataFailed(errorMsg: String) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是 初始页 说明是刷新 界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) {_, view ->
                //设置错误页文字错误提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0 说明是加载更多时出现的错误,设置recyclerView加载错误
            swipe_recycler_view.loadMoreError(0, errorMsg)
        }
    }

    /**
     * 收藏回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了,当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if(event.login) {
            event.collectIds.forEach {
                for (item in adapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了 把所有的收藏全部变为未收藏
            for (item in adapter.data) {
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 在详情中收藏时,接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var  indexResult = -1
                for (index in adapter.data.indices) {
                    if (adapter.data[index].id == event.id) {
                        adapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run {
                if (await() != -1) {
                    adapter.notifyItemChanged(await())
                }
            }
        }
    }





}
