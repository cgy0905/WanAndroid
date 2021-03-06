package me.hegj.wandroid.mvp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.flow_layout.view.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.ColorUtil
import me.hegj.wandroid.mvp.model.entity.ArticleResponse
import me.hegj.wandroid.mvp.model.entity.NavigationResponse

class NavigationAdapter(data: MutableList<NavigationResponse>?) : BaseQuickAdapter<NavigationResponse, BaseViewHolder>(R.layout.item_system, data) {

    lateinit var tagClickListener: OnTagClickListener

    override fun convert(helper: BaseViewHolder?, item: NavigationResponse?) {
        item?.let {
            helper?.setText(R.id.item_system_title, it.name)
            helper?.getView<TagFlowLayout>(R.id.item_system_flowlayout)?.run {
                adapter = object : TagAdapter<ArticleResponse>(it.articles) {
                    override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: ArticleResponse?): View {
                        return LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout, this@run, false)
                                .apply {
                                    flow_tag.text = hotSearchBean?.title
                                    flow_tag.setTextColor(ColorUtil.randomColor())
                                }
                    }
                }
                setOnTagClickListener { view, position, parent ->
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