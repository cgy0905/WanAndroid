package com.cgy.wandroid.ui.adapter

import android.text.Html
import android.text.TextUtils
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.weight.CollectView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/20 15:48
 */
class ArticleAdapter(data : ArrayList<ArticleResponse>?) : BaseQuickAdapter<ArticleResponse, BaseViewHolder>(data) {
    private var mOnCollectViewClickListener : OnCollectViewClickListener?  = null
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
                        helper.setText(R.id.tv_home_author, author)
                        helper.setText(R.id.tv_home_content, Html.fromHtml(title))
                        helper.setText(R.id.tv_home_type2, "$superChapterName·$chapterName")
                        helper.setText(R.id.tv_home_date, niceDate)
                        helper.getView<CollectView>(R.id.cv_home_collect).isChecked = collect
                        if (showTag) {
                            //展示标签
                            helper.setGone(R.id.tv_home_new, fresh)
                            helper.setGone(R.id.tv_home_top, type == 1)
                            if (tags.isNotEmpty()) {
                                helper.setGone(R.id.tv_home_type1, true)
                                helper.setText(R.id.tv_home_type1, tags[0].name)
                            } else {
                                helper.setGone(R.id.tv_home_type1, false)
                            }
                        } else {
                            //隐藏所有标签
                            helper.setGone(R.id.tv_home_top, false)
                            helper.setGone(R.id.tv_home_type1, false)
                            helper.setGone(R.id.tv_home_new, false)
                        }
                    }
                    helper.getView<CollectView>(R.id.cv_home_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                        }

                    })
                }
                ITEM_PROJECT -> {
                    //项目布局的赋值
                    item.run {
                        helper.setText(R.id.tv_project_author, author)
                        helper.setText(R.id.tv_project_title, Html.fromHtml(title))
                        helper.setText(R.id.tv_project_content, Html.fromHtml(desc))
                        helper.setText(R.id.tv_project_type1, "$superChapterName·$chapterName")
                        helper.setText(R.id.tv_project_date, niceDate)
                        if (showTag) {
                            //展示标签
                            helper.setGone(R.id.tv_project_new, fresh)
                            helper.setGone(R.id.tv_project_top, type == 1)
                            if (tags.isNotEmpty()) {
                                helper.setGone(R.id.tv_project_type, true)
                                helper.setText(R.id.tv_project_type, tags[0].name)
                            } else {
                                helper.setGone(R.id.tv_project_type, false)
                            }
                        } else {
                            //隐藏所有标签
                            helper.setGone(R.id.tv_project_top, false)
                            helper.setGone(R.id.tv_project_type, false)
                            helper.setGone(R.id.tv_project_new, false)
                        }
                        helper.getView<CollectView>(R.id.cv_project_collect).isChecked = collect
                        ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader().loadImage(mContext.applicationContext,
                                ImageConfigImpl
                                        .builder()
                                        .url(envelopePic)
                                        .imageView(helper.getView(R.id.iv_project))
                                        .errorPic(R.drawable.default_project_img)
                                        .fallback(R.drawable.default_project_img)
                                        .isCrossFade(true)
                                        .build())
                    }
                    helper.getView<CollectView>(R.id.cv_project_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                        }

                    })
                }
            }
        }
    }
    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(helper: BaseViewHolder, v : CollectView, position : Int)
    }
}