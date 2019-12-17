package com.cgy.wandroid.ui.main.tree.adapter

import android.view.LayoutInflater
import android.view.View
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.ClassifyResponse
import com.cgy.wandroid.mvp.model.entity.SystemResponse
import com.cgy.wandroid.util.ColorUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.flow_layout.view.*

/**
 * @author: cgy
 * @date 2019/12/17 11:44
 * @description:
 */
class SystemAdapter(data: MutableList<SystemResponse>?) : BaseQuickAdapter<SystemResponse, BaseViewHolder>(R.layout.item_system, data) {

    lateinit var tagClickListener: OnTagClickListener
    override fun convert(helper: BaseViewHolder?, item: SystemResponse?) {
        item?.let {
            helper?.setText(R.id.tv_system_title, it.name)
            helper?.getView<TagFlowLayout>(R.id.tag_flow_layout)?.run {
                adapter = object : TagAdapter<ClassifyResponse>(it.children) {
                    override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: ClassifyResponse?): View {
                        return LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout, this@run, false)
                                .apply {
                                    flow_tag.text = hotSearchBean?.name
                                    flow_tag.setTextColor(ColorUtil.randomColor())
                                }
                    }
                }
                setOnTagClickListener { view, positon, parent ->
                    tagClickListener?.onClick(helper.adapterPosition, positon)
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