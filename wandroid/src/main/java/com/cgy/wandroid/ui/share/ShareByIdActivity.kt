package com.cgy.wandroid.ui.share


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.component.DaggerShareByIdComponent
import com.cgy.wandroid.di.module.ShareByIdModule
import com.cgy.wandroid.mvp.contract.ShareByIdContract
import com.cgy.wandroid.mvp.presenter.ShareByIdPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.mvp.model.entity.ShareResponse
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
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.activity_share_by_id.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_recyclerview.view.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description:
 * @date: 2020/01/08 17:42
 */
class ShareByIdActivity : BaseActivity<ShareByIdPresenter>(), ShareByIdContract.View {


    var id: Int = 0
    lateinit var loadSir: LoadService<Any>
    lateinit var adapter: ArticleAdapter
    private var initPageNo = 1
    private var pageNo: Int = initPageNo //当前页码
    private var footView: DefineLoadMoreView? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerShareByIdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .shareByIdModule(ShareByIdModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_share_by_id //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "他的信息"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        ll_share.setBackgroundColor(SettingUtil.getColor(this))
        ArmsUtils.obtainAppComponentFromContext(this).imageLoader().loadImage(this.applicationContext,
                ImageConfigImpl
                        .builder()
                        .url("https://avatars2.githubusercontent.com/u/18655288?s=460&v=4")
                        .imageView(iv_share_logo)
                        .errorPic(R.drawable.ic_account)
                        .fallback(R.drawable.ic_account)
                        .placeholder(R.drawable.ic_account)
                        .isCrossFade(true)
                        .isCircle(true)
                        .build())
        id = intent.getIntExtra("id", 0)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(ll_share) {
            //界面加载失败,或者没有数据时,点击重试的监听
            loadSir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getShareData(pageNo, id)
        }.apply {
            SettingUtil.setLoadingColor(this@ShareByIdActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter
        adapter = ArticleAdapter(arrayListOf(), showTag = true, clickable = false).apply {
            if (SettingUtil.getListMode(this@ShareByIdActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@ShareByIdActivity))
            } else {
                closeLoadAnimation()
            }
            setOnCollectViewClickListener(object : ArticleAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    //点击爱心收藏操作
                    if (v.isChecked) {
                        mPresenter?.unCollect(data[position].id, position)
                    } else {
                        mPresenter?.collect(data[position].id, position)
                    }
                }
            })
            setOnItemClickListener { adapter, view, position ->
                launchActivity(Intent(this@ShareByIdActivity, WebViewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", this@ShareByIdActivity.adapter.data[position])
                        putString("tag", this@ShareByIdActivity::class.java.simpleName)
                        putInt("position", position)
                    })
                })
            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(this@ShareByIdActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前recyclerView最后一个视图位置索引大于等于40,则迅速返回顶部,否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    swipe_recycler_view.scrollToPosition(0) //没有动画迅速返回到顶部
                } else {
                    swipe_recycler_view.smoothScrollToPosition(0) //有滚动动画返回到顶部
                }
            }
        }
        //初始化 swipeRefreshLayout
        swipe_refresh_layout.run {
            setColorSchemeColors(SettingUtil.getColor(this@ShareByIdActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getShareData(pageNo, this@ShareByIdActivity.id)
            }
        }
        //初始化recyclerView
        footView = RecyclerViewUtils().initRecyclerView(this, swipe_recycler_view, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getShareData(pageNo, id)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(this@ShareByIdActivity))
        }
        //监听recyclerView滑动到顶部的时候 需要把向上返回顶部的按钮隐藏
        swipe_recycler_view.run {
            adapter = this@ShareByIdActivity.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("RestrictedApi")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(-1)) {
                        float_action_btn.visibility = View.INVISIBLE
                    }
                    this@ShareByIdActivity.swipe_refresh_layout.isEnabled = recyclerView.childCount == 0 || recyclerView.getChildAt(0).top >= 0
                }
            })
            //发起请求
            mPresenter?.getShareData(pageNo, id)

        }

    }

    override fun requestDataSuccess(shareResponse: ShareResponse) {
       tv_share_name.text = shareResponse.coinInfo.username
        tv_share_info.text = "积分 : ${shareResponse.coinInfo.coinCount} 排名 : ${shareResponse.coinInfo.rank}"
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo && shareResponse.shareArticles.datas.size == 0) {
            //如果是第一页,并且没有数据,页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新的话, floatButton就隐藏,因为这是肯定是在顶部的
            float_action_btn.visibility = View.INVISIBLE
            adapter.setNewData(shareResponse.shareArticles.datas)
        } else {
            //不是第一页
            loadSir.showSuccess()
            adapter.addData(shareResponse.shareArticles.datas)
        }
        pageNo++
        if (shareResponse.shareArticles.pageCount >= pageNo) {
            //如果总条数大于当前页数时,还有更多数据
            swipe_recycler_view.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            swipe_recycler_view.postDelayed({
                //解释一下为什么这里要延时0.2秒操作
                //因为上面的adapter.addData(data) 数据刷新了适配器,是需要时间的,还没刷新完,这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug,刷新最后一页的时候,没有更多数据啦提示先展示出来了,然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下,如果觉得没什么影响的可以去掉这个延时操作,或者有更好的解决方式可以告诉我一下
                swipe_recycler_view.loadMoreFinish(false, false)
            }, 200)
        }
    }

    override fun requestDataFailed(errorMsg: String) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是初始页 说明是刷新,界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) {_, view ->
                //设置错误页文字提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0 说明是加载更多时出现的错误 设置recyclerView加载错误
            swipe_recycler_view.loadMoreError(0, errorMsg)
        }
    }

    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus刷新数据
     */
    @Subscribe
    fun freshLogin(event : LoginFreshEvent) {
        //如果是登录了,当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in adapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了,把所有的收藏全部变为未收藏
            for (item in adapter.data) {
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 接收到收藏文章的Event
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var indexResult = -1
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
