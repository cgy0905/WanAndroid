package com.cgy.wandroid.util

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: cgy
 * @date 2019/12/17 13:56
 * @description: recyclerView的条目分割线
 */
class SpaceItemDecoration //leftRight尉横向间的距离 topBottom为纵向间的距离
(private val leftRight : Int, private val topBottom : Int) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    @SuppressLint("WrongConstant")
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        //数值方向的
        if (layoutManager!!.orientation == LinearLayoutManager.VERTICAL) {
            //最后一项需要botton
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount -1) {
                outRect.bottom = topBottom
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.right = leftRight
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.right = leftRight
            }
            outRect.top = topBottom
            outRect.left = leftRight
            outRect.bottom = topBottom
        }
        super.getItemOffsets(outRect, view, parent, state)
    }
}