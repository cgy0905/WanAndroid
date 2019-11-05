package com.cgy.wandroid.ui.web

import android.content.Intent
import android.os.Bundle
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.di.component.DaggerWebViewComponent
import com.cgy.wandroid.di.module.WebViewModule
import com.cgy.wandroid.mvp.contract.WebViewContract
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.mvp.model.entity.BannerResponse
import com.cgy.wandroid.mvp.presenter.WebViewPresenter
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils
import com.just.agentweb.AgentWeb
import me.hegj.wandroid.mvp.model.entity.enums.CollectType

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

    }


    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showMessage(message: String) {
        ArmsUtils.snackbarText(message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }

    override fun killMyself() {
        finish()
    }
}
