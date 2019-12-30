package com.cgy.wandroid.ui.collect.adapter

import android.text.Html
import android.text.TextUtils
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.CollectResponse
import com.cgy.wandroid.weight.CollectView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils

/**
 * @author: cgy
 * @date 2019/12/26 10:57
 * @description:
 */
class CollectAdapter (data : ArrayList<CollectResponse>?) : BaseQuickAdapter<CollectResponse, BaseViewHolder>(data){
    private var mOnCollectViewClickListener : OnCollectViewClickListener? = null
    private val ARTICLE = 1 //文章类型
    private val PROJECT = 2 //项目类型
    private var showTag = false //是否展示标签 tag 一般主页才用的到

    constructor(data: ArrayList<CollectResponse>?, showTag : Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        //初始化
        multiTypeDelegate = object : MultiTypeDelegate<CollectResponse>() {
            override fun getItemType(entity: CollectResponse): Int {
                //根据是否有图片 判断为文章还是项目
                return if (TextUtils.isEmpty(entity.envelopePic)) ARTICLE else PROJECT
            }
        }
        //注册多布局
        multiTypeDelegate
                .registerItemType(ARTICLE, R.layout.item_article)
                .registerItemType(PROJECT, R.layout.item_project)
    }

    override fun convert(helper: BaseViewHolder, item: CollectResponse?) {
        if (item != null) {
            when (helper.itemViewType) {
                ARTICLE -> {
                    //文章布局的赋值
                    item.run {
                        helper.setText(R.id.tv_home_author, author)
                        helper.setText(R.id.tv_home_content, Html.fromHtml(title))
                        helper.setText(R.id.tv_home_type2, chapterName)
                        helper.setText(R.id.tv_home_date, niceDate)
                        helper.getView<CollectView>(R.id.cv_home_collect).isChecked = true
                        //隐藏所有标签
                        helper.setGone(R.id.tv_home_top, false)
                        helper.setGone(R.id.tv_home_type1, false)
                        helper.setGone(R.id.tv_home_new, false)
                    }
                    helper.getView<CollectView>(R.id.cv_home_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener{
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper,v, helper.adapterPosition)
                        }
                    })
                }
                PROJECT -> {
                    //项目布局的赋值
                    item.run {
                        helper.setText(R.id.tv_project_author,author)
                        helper.setText(R.id.tv_project_title, Html.fromHtml(title))
                        helper.setText(R.id.tv_project_content, Html.fromHtml(desc))
                        helper.setText(R.id.tv_project_type, chapterName)
                        helper.setText(R.id.tv_project_date, niceDate)

                        //隐藏所有标签
                        helper.setGone(R.id.tv_project_top, false)
                        helper.setGone(R.id.tv_project_type1, false)
                        helper.getView<CollectView>(R.id.cv_project_collect).isChecked = true
                        ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader().loadImage(mContext.applicationContext,
                                ImageConfigImpl.builder()
                                        .url(envelopePic)
                                        .imageView(helper.getView(R.id.iv_project))
                                        .isCrossFade(true)
                                        .imageRadius(8)
                                        .build())
                    }
                    helper.getView<CollectView>(R.id.cv_project_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener{
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
        fun onClick(helper: BaseViewHolder, v: CollectView, position: Int)
    }
}