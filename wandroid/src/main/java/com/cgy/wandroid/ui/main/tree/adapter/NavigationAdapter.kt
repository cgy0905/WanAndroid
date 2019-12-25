package com.cgy.wandroid.ui.main.tree.adapter

import android.view.LayoutInflater
import android.view.View
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.ArticleResponse
import com.cgy.wandroid.mvp.model.entity.NavigationResponse
import com.cgy.wandroid.util.ColorUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.flow_layout.view.*

/**
 * @author: cgy
 * @date 2019/12/25 11:37
 * @description:
 */
class NavigationAdapter(data : MutableList<NavigationResponse>?) : BaseQuickAdapter<NavigationResponse, BaseViewHolder>(R.layout.item_system, data) {
    lateinit var tagClickListener: OnTagClickListener
    override fun convert(helper: BaseViewHolder?, item: NavigationResponse?) {
        item?.let {
            helper?.setText(R.id.tv_system_title, it.name)
            helper?.getView<TagFlowLayout>(R.id.tag_flow_layout)?.run {
                adapter = object : TagAdapter<ArticleResponse>(it.articles){
                    override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: ArticleResponse?): View {
                        return LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout, this@run, false)
                                .apply {
                                    flow_tag.text = hotSearchBean?.title
                                    flow_tag.setTextColor(ColorUtil.randomColor())
                                }
                    }
                }
                setOnTagClickListener{view, position, parent ->
                    tagClickListener?.onClick(helper.adapterPosition, position)
                    false
                }
            }
        }
    }

    fun setOnTagClickListener(tagClickListener: OnTagClickListener) {
        this.tagClickListener = tagClickListener
    }

    interface OnTagClickListener {
        fun onClick(position: Int, childPosition: Int)
    }
}