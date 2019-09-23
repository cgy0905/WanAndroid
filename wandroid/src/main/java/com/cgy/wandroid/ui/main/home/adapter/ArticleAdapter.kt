package com.cgy.wandroid.ui.main.home.adapter

import android.text.TextUtils
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.weight.CollectView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/20 15:48
 */
class ArticleAdapter(data : ArrayList<ArticleResponse>?) : BaseQuickAdapter<ArticleResponse, BaseViewHolder>(data) {
    private var mOnCollectViewClickListener : CollectView.OnCollectViewClickListener?  = null
    private val ITEM_ARTICLE = 1//文章类型
    private val ITEM_PROJECT = 2 //项目类型
    private var showTag = false//是否展示标签 tag 一般主页才用的到

    constructor(data: ArrayList<ArticleResponse>?, showTag : Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        //初始化
        multiTypeDelegate = object : MultiTypeDelegate<ArticleResponse>() {
            override fun getItemType(entity: ArticleResponse): Int {
                return if (TextUtils.isEmpty(entity.envelopePic)) ITEM_ARTICLE else ITEM_PROJECT
            }
        }
        //注册多布局
        multiTypeDelegate
                .registerItemType(ITEM_ARTICLE, R.layout.item_article)
                .registerItemType(ITEM_PROJECT, R.layout.item_project)
    }

    override fun convert(helper: BaseViewHolder, item: ArticleResponse?) {
        if (item != null) {
            when (helper.itemViewType) {
                ITEM_ARTICLE -> {
                    //文章布局的赋值
                    item.run {
                        helper.setText(R.id.)
                    }
                }
            }
        }
    }
}