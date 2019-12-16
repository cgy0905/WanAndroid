package com.cgy.wandroid.ui.main.home.search

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.weight.loadCallback.EmptyCallback
import com.cgy.wandroid.weight.loadCallback.ErrorCallback

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.hegj.wandroid.app.event.CollectEvent
import org.greenrobot.eventbus.Subscribe
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.cgy.wandroid.di.component.DaggerSearchResultComponent
import com.cgy.wandroid.di.module.SearchResultModule
import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.cgy.wandroid.mvp.presenter.SearchResultPresenter
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.ui.main.home.adapter.ArticleAdapter
import com.cgy.wandroid.ui.web.WebViewActivity
import com.cgy.wandroid.util.RecyclerViewUtils
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.CollectView
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.chad.library.adapter.base.BaseViewHolder
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * @author: cgy
 * @description: 搜索结果
 * @date: 2019/12/16 13:50
 */
class SearchResultActivity : BaseActivity<SearchResultPresenter>(), SearchResultContract.View {


    private var initPageNo = 0 //注意 页码是从0开始
    var pageNo = initPageNo
    lateinit var loadSir: LoadService<Any>
    lateinit var searchKey: String//搜索关键词
    lateinit var articleAdapter: ArticleAdapter//适配器
    private var footView: DefineLoadMoreView? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSearchResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchResultModule(SearchResultModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_search_result //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        searchKey = intent.getStringExtra("searchKey")
        toolbar.run {
            setSupportActionBar(this)
            title = searchKey
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(swipe_refresh_layout) {
            //界面加载失败,或者没有数据时,点击重试的监听
            loadSir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getArticleList(pageNo, searchKey)

        }.apply {
            SettingUtil.setLoadingColor(this@SearchResultActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter 并设置监听
        articleAdapter = ArticleAdapter(arrayListOf(), true).apply {
            if (SettingUtil.getListMode(this@SearchResultActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@SearchResultActivity))
            } else {
                closeLoadAnimation()
            }
            setOnCollectViewClickListener(object : ArticleAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    //点击了爱心收藏执行操作
                    if (v.isChecked) {
                        mPresenter?.unCollect(data[position].id, position)
                    } else {
                        mPresenter?.collect(data[position].id, position)
                    }
                }

            })

            setOnItemClickListener { _, view, position ->
                //点击了整行
                val intent = Intent(this@SearchResultActivity, WebViewActivity::class.java)
                val bundle = Bundle().also {
                    it.putSerializable("data", data[position])
                    it.putString("tag", this@SearchResultActivity::class.java.simpleName)
                    it.putInt("position", position)
                }
                intent.putExtras(bundle)
                launchActivity(intent)

            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(this@SearchResultActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前recyclerView最后一个视图位置的索引大于等于40,则迅速返回顶部,否则带有滚动动画效果返回顶部
                if (layoutManager.findFirstVisibleItemPosition() >= 40) {
                    swipe_recycler_view.scrollToPosition(0)//没有动画迅速返回顶部
                } else {
                    swipe_recycler_view.smoothScrollToPosition(0)
                }
            }
        }
        //初始化 swipeRefreshLayout
        swipe_refresh_layout.run {
            setColorSchemeColors(SettingUtil.getColor(this@SearchResultActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getArticleList(pageNo, searchKey)
            }
        }

        //初始化recyclerView
        footView = RecyclerViewUtils().initRecyclerView(this, swipe_recycler_view, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getArticleList(pageNo, searchKey)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(this@SearchResultActivity))
        }

        //监听recyclerView滑动到顶部的时候,需要把向上返回顶部的按钮隐藏
        swipe_recycler_view.run {
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
        mPresenter?.getArticleList(pageNo, searchKey)//发起请求
    }

    @SuppressLint("RestrictedApi")
    override fun requestArticleSuccess(articles: ApiPagerResponse<MutableList<ArticleResponse>>) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo && articles.datas.size == 0) {
            //如果是第一页,并且没有数据 页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新的话,floatbutton就隐藏 因为这时候肯定要在顶部的
            float_action_btn.visibility = View.INVISIBLE
            articleAdapter.setNewData(articles.datas)
        } else {
            //不是第一页
            loadSir.showSuccess()
            articleAdapter.addData(articles.datas)
        }
        pageNo++
        if (articles.pageCount >= pageNo) {
            //如果总条数大于当前页数时,还要更多数据
            swipe_recycler_view.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            swipe_recycler_view.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                swipe_recycler_view.loadMoreFinish(false, false)
            }, 200)
        }
    }

    /**
     * 获取文章失败
     */
    override fun requestArticleFailed(errorMsg: String) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是 初始页 说明是刷新 界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
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
     * 收藏文章的回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, articleAdapter.data[position].id).post()
    }

    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了 当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in articleAdapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了 把所有的收藏全部变为未收藏
            for (item in articleAdapter.data) {
                item.collect = false
            }
        }
        articleAdapter.notifyDataSetChanged()
    }

    /**
     * 在详情中收藏时, 接收到EventBus
     */
    @Subscribe
    fun collectChange(event : CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var indexResult = -1
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == event.id) {
                        articleAdapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run {
                if (await() != -1) {
                    articleAdapter.notifyItemChanged(await())
                }
            }
        }
    }
}
