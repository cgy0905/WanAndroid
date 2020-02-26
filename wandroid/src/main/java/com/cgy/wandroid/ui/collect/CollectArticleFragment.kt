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
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerCollectComponent
import com.cgy.wandroid.di.module.CollectModule
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.mvp.contract.CollectContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.CollectResponse
import com.cgy.wandroid.mvp.presenter.CollectPresenter
import com.cgy.wandroid.ui.adapter.CollectAdapter
import com.cgy.wandroid.ui.web.WebViewActivity
import com.cgy.wandroid.util.RecyclerViewUtils
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.CollectView
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.cgy.wandroid.weight.loadCallback.EmptyCallback
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description: 收藏文章的fragment
 * @date: 2019/12/26 09:36
 */
class CollectArticleFragment : BaseFragment<CollectPresenter>(), CollectContract.View {


    lateinit var loadSir : LoadService<Any>
    lateinit var adapter : CollectAdapter
    var initPageNo = 0 //分页页码初始值 收藏文章列表是从0开始的
    var pageNo = initPageNo
    private var footView : DefineLoadMoreView? = null
    companion object {
        fun newInstance(): CollectArticleFragment {
            return CollectArticleFragment()
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerCollectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .collectModule(CollectModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.swipe_refresh_layout)) {
            loadSir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            pageNo = initPageNo
            mPresenter?.getCollectDataByType(pageNo)
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
                pageNo = initPageNo
                mPresenter?.getCollectDataByType(pageNo)
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
            mPresenter?.getCollectDataByType(pageNo)
        })
        //初始化recyclerView
        swipe_recycler_view.run {
            layoutManager = LinearLayoutManager(_mActivity)
            setHasFixedSize(true)
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
        //初始化adapter
        adapter = CollectAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(_mActivity)!= 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            //点击了爱心
            setOnCollectViewClickListener(object : CollectAdapter.OnCollectViewClickListener{
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    mPresenter?.unCollect(adapter.data[position].id, adapter.data[position].originId, position)
                }
            })
            //点击了整行
            setOnItemClickListener { _, view, position ->
                val intent = Intent(_mActivity, WebViewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("collect", adapter.data[position])
                    putString("tag", this@CollectArticleFragment::class.java.simpleName)
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
        mPresenter?.getCollectDataByType(pageNo)//请求数据
    }

    @SuppressLint("RestrictedApi")
    override fun requestDataSuccess(apiPagerResponse: ApiPagerResponse<MutableList<CollectResponse>>) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo && apiPagerResponse.datas.size == 0) {
            //如果是第一页,并且没有数据,页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新的话 floatActionBtn隐藏,这个时候一定是在顶部的
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
            loadSir.setCallBack(EmptyCallback::class.java){_, view ->
                //设置错误页文字提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg

            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是1 说明是加载更多时出现的错误 设置recyclerView加载错误
            swipe_recycler_view.loadMoreError(0, errorMsg)
        }
    }

    override fun unCollect(position: Int) {
        //通知点其他的页面刷新一下这个数据
        CollectEvent(false, adapter.data[position].originId, this::class.java.simpleName).post()
        //当前收藏数据大于1条的时候,直接删除
        if (adapter.data.size > 1) {
            adapter.remove(position)
        } else {
            //小于等于1条时,不要删除了,直接给界面设置成空数据
            loadSir.showCallback(EmptyCallback::class.java)
        }
    }

    override fun unCollectFailed(position: Int) {
        adapter.notifyItemChanged(position)
    }

    @Subscribe
    fun collectChange(event: CollectEvent) {
        //如果tag不是当前类名 需要刷新
        if (this::class.java.simpleName != event.tag) {
            swipe_refresh_layout.isRefreshing = true
            pageNo = initPageNo
            mPresenter?.getCollectDataByType(pageNo)
        }
    }


}
