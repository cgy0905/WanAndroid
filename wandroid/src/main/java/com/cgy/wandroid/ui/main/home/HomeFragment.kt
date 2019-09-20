
package com.cgy.wandroid.ui.main.home


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerHomeComponent
import com.cgy.wandroid.di.module.HomeModule
import com.cgy.wandroid.mvp.contract.HomeContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.mvp.model.entity.BannerResponse
import com.cgy.wandroid.mvp.presenter.HomePresenter
import com.cgy.wandroid.ui.main.home.adapter.ArticleAdapter
import com.cgy.wandroid.ui.main.home.search.SearchActivity
import com.cgy.wandroid.ui.web.WebViewActivity
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.cgy.wandroid.weight.loadCallback.EmptyCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
import com.jess.arms.di.component.AppComponent
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_banner.view.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * @author：cgy
 * @description：主页
 * @date：09/19/2019 15:00
 */
class HomeFragment : BaseFragment<HomePresenter>(), HomeContract.View {


    private var initPageNo = 0 //注意,首页的页码是从0开始的
    var pageNo = initPageNo
    lateinit var adapter : ArticleAdapter
    lateinit var loadSir : LoadService<Any>
    private var footView : DefineLoadMoreView? = null

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
                setDelegate{_, _, _, position ->
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

        }
    }

    override fun requestArticleFailed(errorMsg: String) {

    }

    override fun collect(collected: Boolean, position: Int) {

    }


}
