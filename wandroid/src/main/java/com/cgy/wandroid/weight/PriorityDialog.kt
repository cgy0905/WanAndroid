package com.cgy.wandroid.weight

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.enums.TodoType
import com.cgy.wandroid.ui.adapter.PriorityAdapter
import com.cgy.wandroid.util.GridDividerItemDecoration
import kotlinx.android.synthetic.main.dialog_todo.view.*
import net.lucode.hackware.magicindicator.buildins.UIUtil

/**
 * @author: cgy
 * @date 2020/1/8 14:55
 * @description:
 */
class PriorityDialog (context: Context, type : Int) : Dialog (context, R.style.BottomDialogStyle) {
    private lateinit var shareAdapter : PriorityAdapter
    private var priorityInterface : PriorityInterface? = null
    private var priorityData: ArrayList<TodoType> = ArrayList()
    var type = TodoType.TodoType1.type
    init {
        this.type = type
        //拿到Dialog的window 修改window的属性
        val window = window
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.decorView.setPadding(0, 0, 0, 0)
        //获取window的LayoutParams
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        val attributes = window.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.gravity = Gravity.BOTTOM
        //一定要重新设置才能生效
        window.attributes = attributes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //添加数据
        TodoType.values().forEach {
            priorityData.add(it)
        }
        //初始化adapter
        shareAdapter = PriorityAdapter(priorityData, type).apply {
            setOnItemClickListener { adapter, view1, position ->
                priorityInterface?.run {
                    onSelect(priorityData[position])
                }
                dismiss()
            }
        }
        //初始化recyclerView
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_todo, null).apply {
            recycler_view.apply {
                layoutManager = GridLayoutManager(context, 3)
                setHasFixedSize(true)
                addItemDecoration(GridDividerItemDecoration(context, 0, UIUtil.dip2px(context, 24.0), false))
                adapter = shareAdapter
            }
        }
        setContentView(view)
    }
    fun setPriorityInterface(priorityInterface: PriorityInterface) {
        this.priorityInterface = priorityInterface
    }

    interface PriorityInterface {
        fun onSelect(type: TodoType)
    }
}