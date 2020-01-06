package com.cgy.wandroid.ui.adapter

import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.TodoResponse
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * @author: cgy
 * @date 2020/1/6 13:44
 * @description:
 */
class TodoAdapter (data : ArrayList<TodoResponse> ?) : BaseQuickAdapter<TodoResponse, BaseViewHolder>(R.layout.item_todo, data){
    override fun convert(helper: BaseViewHolder?, item: TodoResponse?) {

    }
}