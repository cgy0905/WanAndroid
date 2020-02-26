package com.cgy.wandroid.ui.adapter

import android.text.Html
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.CollectUrlResponse
import com.cgy.wandroid.weight.CollectView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author: cgy
 * @date 2019/12/30 17:30
 * @description:
 */
class CollectUrlAdapter(data : ArrayList<CollectUrlResponse>?) : BaseQuickAdapter<CollectUrlResponse, BaseViewHolder>(R.layout.item_collect_url){
    private var mOnCollectViewClickListener : OnCollectViewClickListener? = null

    override fun convert(helper: BaseViewHolder, item: CollectUrlResponse?) {
        if (item != null) {
            //赋值
            item.run {
                helper.setText(R.id.tv_collectUrl_name, Html.fromHtml(name))
                helper.setText(R.id.tv_collectUrl_link, link)
                helper.getView<CollectView>(R.id.tv_project_content).isChecked = true
            }
            helper.getView<CollectView>(R.id.cv_collectUrl_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener{
                override fun onClick(v: CollectView) {
                    mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                }

            })
        }
    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(helper: BaseViewHolder, v: CollectView, position: Int)
    }
}