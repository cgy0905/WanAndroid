package com.cgy.wandroid.ui.main.home


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.bgabanner.BGABanner
import com.blankj.utilcode.util.ToastUtils
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerHomeComponent
import com.cgy.wandroid.di.module.HomeModule
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.contract.HomeContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.mvp.model.entity.BannerResponse
import com.cgy.wandroid.mvp.presenter.HomePresenter
import com.cgy.wandroid.ui.adapter.ArticleAdapter
import com.cgy.wandroid.ui.main.home.search.SearchActivity
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
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_banner.view.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject


/**
 * @author：cgy
 * @description：主页
 * @date：09/19/2019 15:00
 */
class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.View {


    private var initPageNo = 0 //注意,首页的页码是从0开始的
    var pageNo = initPageNo

    @Inject
    lateinit var adapter: ArticleAdapter

    lateinit var loadSir: LoadService<Any>
    private var footView: DefineLoadMoreView? = null

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(HomeModule(this))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)//加上这句话menu才会显示出来
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.swipe_refresh_layout)) {
            //界面加载失败,或者没有数据时,点击重试的监听
            loadSir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getBanner()
            mPresenter?.getArticleList(pageNo)

        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return rootView
    }

    /**
     * fragment初始化时会调用该方法
     */
    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        toolbar.run {
            setBackgroundColor(SettingUtil.getColor(_mActivity))
            title = "首页"
            inflateMenu(R.menu.menu_home)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> launchActivity(Intent(_mActivity, SearchActivity::class.java))
                }
                true
            }

        }
        adapter = ArticleAdapter(arrayListOf(), true).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            setOnCollectViewClickListener(object : ArticleAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    //点击爱心收藏执行操作
                    if (v.isChecked) {
                        //注意: 这里因为RecyclerView添加了头部,所以这个索引要减去头部的count才是实际的position
                        mPresenter?.unCollect(data[position - swipe_recycler_view.headerCount].id, position - swipe_recycler_view.headerCount)
                    } else {
                        mPresenter?.collect(data[position - swipe_recycler_view.headerCount].id, position - swipe_recycler_view.headerCount)
                    }
                }

            })
            setOnItemClickListener { _, view, position ->
                //点击了整行
                //注意: 这里因为RecyclerView添加了头部,所以这个索引要减去头部的count才是实际的position
                val intent = Intent(_mActivity, WebViewActivity::class.java)
                val bundle = Bundle().also {
                    it.putSerializable("data", data[position - swipe_recycler_view.headerCount])
                    it.putString("tag", this@HomeFragment::class.java.simpleName)
                    it.putInt("position", position - swipe_recycler_view.headerCount)
                }
                intent.putExtras(bundle)
                startActivity(intent)

            }
        }
        float_action_btn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = swipe_recycler_view.layoutManager as LinearLayoutManager
                //如果当前RecyclerView 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    swipe_recycler_view.scrollToPosition(0)//没有动画迅速返回到顶部
                } else {
                    swipe_recycler_view.smoothScrollToPosition(0)//有滚动动画返回到顶部
                }
            }
        }
        swipe_refresh_layout.run {
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getArticleList(pageNo)
            }
        }
        //初始化RecyclerView
        footView = RecyclerViewUtils().initRecyclerView(_mActivity, swipe_recycler_view, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getArticleList(pageNo)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        }
        swipe_recycler_view.run {
            //监听RecyclerView滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
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
        mPresenter?.getBanner()
        mPresenter?.getArticleList(pageNo)
    }

    //获取banner数据成功
    override fun requestBannerSuccess(banners: MutableList<BannerResponse>) {
        val view = LayoutInflater.from(_mActivity).inflate(R.layout.include_banner, null).apply {
            banner.run {
                setAdapter(BGABanner.Adapter<ImageView, BannerResponse> { _: BGABanner, view: ImageView, banner: BannerResponse?, i: Int ->
                    ArmsUtils.obtainAppComponentFromContext(_mActivity).imageLoader().loadImage(_mActivity.applicationContext,
                            ImageConfigImpl
                                    .builder()
                                    .url(banner?.imagePath)
                                    .imageView(view)
                                    .isCrossFade(true)
                                    .build())
                })
                setDelegate { _, _, _, position ->
                    launchActivity(Intent(_mActivity, WebViewActivity::class.java).apply {
                        putExtras(Bundle().apply {
                            putSerializable("bannerData", banners[position])
                            putString("tag", "banner")
                        })
                    })
                }
                setData(banners, null)
            }
        }
        //将banner添加到recyclerView的头部
        if (swipe_recycler_view.headerCount == 0) swipe_recycler_view.addHeaderView(view)
    }

    /**
     * 获取文章数据成功
     */
    @SuppressLint("RestrictedApi")
    override fun requestArticleSuccess(articles: ApiPagerResponse<MutableList<ArticleResponse>>) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo && articles.datas.size == 0) {
            //如果是第一页,并且没有数据,页面提示空布局
            loadSir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadSir.showSuccess()
            //如果是刷新,floatBtn隐藏
            float_action_btn.visibility = View.INVISIBLE
            adapter.setNewData(articles.datas)
        } else {
            //不是第一页
            loadSir.showSuccess()
            adapter.addData(articles.datas)
        }
        pageNo++
        if (articles.pageCount >= pageNo) {
            //如果总条数大于当前页数 还有更多数据
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
     * 获取文章数据失败
     */
    override fun requestArticleFailed(errorMsg: String) {
        swipe_refresh_layout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是初始页 说明是刷新 界面切换成错误页
            loadSir.setCallBack(ErrorCallback::class.java) { _, view ->
                //设置错误页文字错误提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0 说明是加载更多时出现的错误 设置RecyclerView加载错误
            swipe_recycler_view.loadMoreError(0, errorMsg)
        }
    }

    /**
     * 收藏文章回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录 当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
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
     * 接收到收藏文章的event
     */
    @Subscribe
    fun collectChange(event : CollectEvent) {
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

    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        toolbar.setBackgroundColor(SettingUtil.getColor(_mActivity))
        float_action_btn.backgroundTintList =  SettingUtil.getOneColorStateList(_mActivity)
        swipe_refresh_layout.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        SettingUtil.setLoadingColor(_mActivity, loadSir)
        footView?.setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        if (SettingUtil.getListMode(_mActivity) != 0) {
            adapter.openLoadAnimation(SettingUtil.getListMode(_mActivity))
        } else {
            adapter.closeLoadAnimation()
        }
    }
}
