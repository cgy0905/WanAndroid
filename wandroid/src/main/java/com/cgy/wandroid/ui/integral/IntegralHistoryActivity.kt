package com.cgy.wandroid.ui.integral

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.di.component.DaggerIntegralComponent
import com.cgy.wandroid.di.module.IntegralModule
import com.cgy.wandroid.mvp.contract.IntegralContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.IntegralHistoryResponse
import com.cgy.wandroid.mvp.model.entity.IntegralResponse
import com.cgy.wandroid.mvp.presenter.IntegralPresenter
import com.cgy.wandroid.ui.adapter.IntegralHistoryAdapter
import com.cgy.wandroid.util.RecyclerViewUtils
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.cgy.wandroid.weight.loadCallback.EmptyCallback
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * @author: cgy
 * @description: 积分排行历史
 * @date: 2019/12/31 17:10
 */
class IntegralHistoryActivity : BaseActivity<IntegralPresenter>(), IntegralContract.View {
   lateinit var loadSir : LoadService<Any>
    lateinit var adapter : IntegralHistoryAdapter
    private var initPageNo = 1
    private var pageNo : Int = initPageNo //当前页码
    private var footView : DefineLoadMoreView? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerIntegralComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .integralModule(IntegralModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_integral_history //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "积分记录"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(swipe_refresh_layout) {
            //界面加载失败,或者没有数据时,点击重试的监听
            loadSir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getIntegralHistoryData(pageNo)
        }.apply {
            SettingUtil.setLoadingColor(this@IntegralHistoryActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter
        adapter = IntegralHistoryAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(this@IntegralHistoryActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@IntegralHistoryActivity))
            } else {
                closeLoadAnimation()
            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(this@IntegralHistoryActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前recyclerView 最后一个视图位置的索引大于等于40,则迅速返回顶部,否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    swipe_recycler_view.scrollToPosition(0)//没有动画迅速返回到顶部(马上)
                } else {
                    swipe_recycler_view.smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
                }
            }
        }
        //初始化swipeRefreshLayout
        swipe_refresh_layout.run {
            setColorSchemeColors(SettingUtil.getColor(this@IntegralHistoryActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getIntegralHistoryData(pageNo)
            }
        }
        //初始化recyclerView
        footView = RecyclerViewUtils().initRecyclerView(this, swipe_recycler_view, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getIntegralHistoryData(pageNo)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(this@IntegralHistoryActivity))
        }

        //监听recyclerView滑动到顶部的时候,需要把向上返回顶部的按钮隐藏
        swipe_recycler_view.run {
            adapter = this@IntegralHistoryActivity.adapter
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
        //发起请求
        mPresenter?.getIntegralHistoryData(pageNo)
    }


    override fun requestDataSuccess(articles: ApiPagerResponse<MutableList<IntegralResponse>>) {

    }

    override fun requestHistoryDataSuccess(articles: ApiPagerResponse<MutableList<IntegralHistoryResponse>>) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo && articles.datas.size == 0) {
            //如果是第一页,并且没有数据,页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新的话,floatActionButton就要隐藏了,因为这时候肯定是在顶部
            float_action_btn.visibility = View.INVISIBLE
            adapter.setNewData(articles.datas)
        } else {
            //不是第一页
            loadSir.showSuccess()
            adapter.addData(articles.datas)
        }
        pageNo++
        if (articles.pageCount >= pageNo) {
            //如果总条数大于当前页数时,还有更多数据
            swipe_recycler_view.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            swipe_recycler_view.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器,是需要时间的,还没刷新完,这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug,刷新最后一页的时候,没有更多数据啦提示先展示出来了,然后才会加载出请求到的数据
                //暂时还没有找到好的方法,就用这个处理一下,如果觉得没什么影响的可以去掉这个延时操作,或者有更好的解决方式可以告诉我一下
                swipe_recycler_view.loadMoreFinish(false, false)
            }, 200)
        }
    }

    override fun requestDataFailed(errorMsg: String) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是 初始页 说明是刷新,界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
                //设置错误页文字提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0, 说明加载更多时出现的错误,设置recyclerView加载错误
            swipe_recycler_view.loadMoreError(0, errorMsg)
        }
    }

}
