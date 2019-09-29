package com.cgy.wandroid.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.cgy.wandroid.weight.DefineLoadMoreView
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/24 13:40
 */
class RecyclerViewUtils {

    fun initRecyclerView(context: Context, recyclerView : SwipeRecyclerView, loadMoreListener: SwipeRecyclerView.LoadMoreListener) : DefineLoadMoreView {
        val footerView = DefineLoadMoreView(context)
        recyclerView.addFooterView(footerView)
        recyclerView.setLoadMoreView(footerView)//添加加载更多尾部
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLoadMoreListener(loadMoreListener)//设置加载更多回调
        footerView.setLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
            //设置尾部点击回调
            footerView.onLoading()
            loadMoreListener.onLoadMore()
        })
        return footerView
    }
}