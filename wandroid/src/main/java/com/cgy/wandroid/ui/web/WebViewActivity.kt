package com.cgy.wandroid.ui.web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.di.component.DaggerWebViewComponent
import com.cgy.wandroid.di.module.WebViewModule
import com.cgy.wandroid.event.CollectEvent
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.mvp.contract.WebViewContract
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.mvp.model.entity.BannerResponse
import com.cgy.wandroid.mvp.model.entity.CollectResponse
import com.cgy.wandroid.mvp.model.entity.CollectUrlResponse
import com.cgy.wandroid.mvp.model.entity.enums.CollectType
import com.cgy.wandroid.mvp.presenter.WebViewPresenter
import com.cgy.wandroid.ui.login.LoginActivity
import com.cgy.wandroid.util.CacheUtil
import com.jess.arms.di.component.AppComponent
import com.jess.arms.integration.AppManager
import com.jess.arms.utils.ArmsUtils
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.app.event.CollectEvent
import me.hegj.wandroid.mvp.model.entity.enums.CollectType
import org.greenrobot.eventbus.Subscribe

/**
 * @author: cgy
 * @description:
 * @date: 2019/09/20 15:41
 */
class WebViewActivity : BaseActivity<WebViewPresenter>(), WebViewContract.View {


    var collect = false//是否收藏
    var id = 0 //id
    lateinit var showTitle : String //标题
    lateinit var url : String

    var collectType = 0 //需要收藏的类型 具体参数说明请看 CollectType枚举类

    private lateinit var mAgentWeb : AgentWeb

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerWebViewComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .webViewModule(WebViewModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_web_view //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        //点击文章进来的
        intent.getSerializableExtra("data")?.let {
            it as ArticleResponse
            id = it.id
            //替换掉部分数据可能包含的网页标签
            showTitle = it.title.replace("<em class='highlight'>", "").replace("</em>", "")
            collect = it.collect
            url = it.link
            collectType = CollectType.Article.type
        }
        //点击首页轮播图进来的
        intent.getSerializableExtra("bannerdata")?.let {
            it as BannerResponse
            id = it.id
            showTitle = it.title.replace("<em class='highlight'>", "").replace("</em>", "")
            collect = false //从首页轮播图 没法判断是否已经收藏过，所以直接默认没有收藏
            url = it.url
            collectType = CollectType.Url.type
        }
        //从收藏文章列表点进来的
        intent.getSerializableExtra("collect")?.let {
            it as CollectResponse
            id = it.originId
            //替换掉部分数据可能包含的网页标签
            showTitle = it.title.replace("<em class='highlight'>","").replace("</em>","")
            collect = true //从收藏列表过来的 肯定是 true
            url = it.link
            collectType = CollectType.Article.type
        }
        //点击收藏网址列表进来的
        intent.getSerializableExtra("collectUrl")?.let {
            it as CollectUrlResponse
            id = it.id
            //替换掉部分数据可能包含的网页标签
            showTitle = it.name.replace("<em class='highlight'>","").replace("</em>","")
            collect = true//从收藏列表过来的, 肯定是true了
            url = it.link
            collectType = CollectType.Url.type
        }
        toolbar.run {
            setSupportActionBar(this)
            title = showTitle
            setNavigationIcon(R.drawable.ic_back)
        }
        //加载网页
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll_webView, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)){
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_web, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //如果收藏了,右上角的图标相对应改变
        if (collect) {
            menu.findItem(R.id.web_collect).icon = ContextCompat.getDrawable(this, R.drawable.ic_collected)
        } else {
            menu.findItem(R.id.web_collect).icon = ContextCompat.getDrawable(this, R.drawable.ic_collect)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.web_share -> {//分享
                startActivity(Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$showTitle:$url")
                    type = "text/plain"
                }, "分享到"))

            }
            R.id.web_refresh -> {//刷新网页
                mAgentWeb.urlLoader.reload()
            }
            R.id.web_collect -> {//点击收藏
                //是否已经登录了,没登录需要跳转到登录页去
                if (CacheUtil.isLogin()) {
                    //是否已经收藏了
                    if (collect) {
                        if (collectType == CollectType.Url.type) {
                            //取消收藏网址
                            mPresenter?.unCollectUrl(id)
                        } else {
                            //取消收藏文章
                            mPresenter?.unCollect(id)
                        }
                    } else {
                        if (collectType == CollectType.Url.type) {
                            //收藏网址
                            mPresenter?.collectUrl(showTitle, url)
                        } else {
                            //收藏文章
                            mPresenter?.collect(id)
                        }
                    }
                } else {
                    //跳转到登录页
                    AppManager.getAppManager().startActivity(Intent(this, LoginActivity::class.java))
                }

            }
            R.id.web_liulanqi -> {
                //用浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 收藏回调 不管成功或失败都会进来
     */
    override fun collect(collected: Boolean) {
        collect = collected
        //刷新一下menu
        window.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
        invalidateOptionsMenu()
        //通知app刷新相对应ID的数据的收藏的值
        CollectEvent(collect, id).post()
    }

    /**
     * 收藏网址成功的回调
     */
    override fun collectUrlSuccess(collected: Boolean, data: CollectUrlResponse) {
        collect = collected
        //刷新一下menu
        window.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
        invalidateOptionsMenu()
        id = data.id
        //通知app刷新相对应ID的数据的收藏的值
        CollectEvent(collected,id).post()
    }

    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了,当前界面的id与账户收藏集合中的id匹配的值需要设置已经收藏 并刷新menu
        if (event.login) {
            event.collectIds.forEach {
                if (it.toInt() == id) {
                    collect = true
                    window.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                    invalidateOptionsMenu()
                    return@forEach
                }
            }
        }
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}
