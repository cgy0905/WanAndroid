package com.cgy.wandroid.ui.adapter

import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.OpenProject
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author: cgy
 * @date 2020/1/3 11:17
 * @description: 开源项目 adapter
 */
class OpenProjectAdapter(data : ArrayList<OpenProject>?) : BaseQuickAdapter<OpenProject, BaseViewHolder>(R.layout.item_open_project, data) {
    override fun convert(helper: BaseViewHolder, item: OpenProject?) {
        //赋值
        item?.run {
            helper.setText(R.id.tv_open_project_name, name)
            helper.setText(R.id.tv_open_project_content, content)
        }
    }
}