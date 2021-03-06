package com.cgy.wandroid.weight

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.cgy.wandroid.R
import com.cgy.wandroid.util.SettingUtil
import com.yanzhenjie.recyclerview.SwipeRecyclerView

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/20 13:39
 */
class DefineLoadMoreView(context: Context) : LinearLayout(context), SwipeRecyclerView.LoadMoreView, View.OnClickListener {

    public val mProgressBar: ProgressBar
    private val mTvMessage: TextView

    private var mLoadMoreListener: SwipeRecyclerView.LoadMoreListener? = null

    fun setLoadMoreListener(loadMoreListener: SwipeRecyclerView.LoadMoreListener) {
        this.mLoadMoreListener = loadMoreListener
    }

    init {
        layoutParams = ViewGroup.LayoutParams(-1, -2)
        gravity = Gravity.CENTER
        visibility = View.GONE

        val displayMetrics = resources.displayMetrics

        val minHeight = (displayMetrics.density * 60 + 0.5).toInt()
        minimumHeight = minHeight

        View.inflate(context, R.layout.layout_footer_loadmore, this)
        mProgressBar = findViewById(R.id.loading_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
            mProgressBar.indeterminateTintList = SettingUtil.getOneColorStateList(context)
        }

        mTvMessage = findViewById(R.id.tv_message)
        setOnClickListener(this)
    }

    /**
     * 显示进度条
     */
    override fun onLoading() {
        visibility = View.VISIBLE
        mProgressBar.visibility = View.VISIBLE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = "正在努力加载，请稍后..."
    }

    /**
     * 调用了setAutoLoadMore(false)后,在需要加载更多的时候,这个方法会被调用,并传入加载更多的listener
     */
    override fun onWaitToLoadMore(loadMoreListener: SwipeRecyclerView.LoadMoreListener?) {
        this.mLoadMoreListener = loadMoreListener

        visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        mTvMessage.visibility = View.VISIBLE
        mTvMessage.text = "点我加载更多"
    }

    /**
     * 加载出错,下面的错误码和错误信息二选一
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    @SuppressLint("LogNotTimber")
    override fun onLoadError(errorCode: Int, errorMessage: String?) {
        visibility = View.VISIBLE
        mProgressBar.visibility = View.GONE
        mTvMessage.visibility = View.VISIBLE
        // 这里不直接设置错误信息,要根据errorCode动态设置错误数据
        mTvMessage.text = errorMessage
        Log.i("cgy","加载失败啦")
    }

    /**
     * 加载更多完成
     * @param dataEmpty 是否请求到空数据。
     * @param hasMore 是否还有更多数据等待请求。
     */
    override fun onLoadFinish(dataEmpty: Boolean, hasMore: Boolean) {
        if (!hasMore) {
            visibility = View.VISIBLE
            if (dataEmpty) {
                mProgressBar.visibility = View.GONE
                mTvMessage.visibility = View.VISIBLE
                mTvMessage.text = "暂时没有数据"
            } else {
                mProgressBar.visibility = View.GONE
                mTvMessage.visibility = View.VISIBLE
                mTvMessage.text = "没有更多数据啦"
            }
        } else {
            visibility = View.INVISIBLE
        }
    }

    /**
     * 非自动加载更多时mLoadMoreListener才不为空
     */
    override fun onClick(v: View?) {
        //为什么加后面那个判断，因为Wandroid第0页能够请求完所有数据的情况下， 再去请求第1页 也能取到值，所以这里要判断没有更多数据的时候禁止在响应点击事件了
        mLoadMoreListener?.let {
            if (mTvMessage.text != "没有更多数据啦"){
                it.onLoadMore()
            }
        }
    }

    fun setLoadViewColor(colorStateList: ColorStateList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProgressBar.indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
            mProgressBar.indeterminateTintList = colorStateList
        }
    }
}