package com.cgy.wandroid.ui.integral.adapter

import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.IntegralHistoryResponse
import com.cgy.wandroid.util.DateTimeUtil
import com.cgy.wandroid.util.SettingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author: cgy
 * @date 2019/12/31 17:49
 * @description:
 */
class IntegralHistoryAdapter (data : ArrayList<IntegralHistoryResponse> ?) : BaseQuickAdapter<IntegralHistoryResponse, BaseViewHolder>(R.layout.item_integral_history, data){
    override fun convert(helper: BaseViewHolder, item: IntegralHistoryResponse?) {
        //赋值
        item?.run {
            val descStr = if (desc.contains("积分")) desc.subSequence(desc.indexOf("积分"), desc.length) else ""
            helper.setText(R.id.tv_integral_history_title, reason+ descStr)
            helper.setText(R.id.tv_integral_history_date, DateTimeUtil.formatDate(date, DateTimeUtil.DATE_PATTERN_SS))
            helper.setText(R.id.tv_integral_history_count, "+$coinCount")
            helper.setTextColor(R.id.tv_integral_history_count, SettingUtil.getColor(mContext))
        }
    }
}