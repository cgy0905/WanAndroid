package com.cgy.wandroid.ui.adapter

import com.cgy.wandroid.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author: cgy
 * @date 2019/12/16 11:54
 * @description:
 */
class SearchHistoryAdapter(data : MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_history, data){
    override fun convert(helper: BaseViewHolder?, item: String?) {
        item?.let {
            helper?.setText(R.id.tv_history_text, it)
            helper?.addOnClickListener(R.id.iv_history_clear)
        }
    }
}