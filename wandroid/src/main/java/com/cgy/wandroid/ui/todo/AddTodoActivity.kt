package com.cgy.wandroid.ui.todo

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.component.DaggerAddTodoComponent
import com.cgy.wandroid.di.module.AddTodoModule
import com.cgy.wandroid.mvp.contract.AddTodoContract
import com.cgy.wandroid.mvp.presenter.AddTodoPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.event.AddTodoEvent
import com.cgy.wandroid.mvp.model.entity.TodoResponse
import com.cgy.wandroid.mvp.model.entity.enums.TodoType
import com.cgy.wandroid.util.DateTimeUtil
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.PriorityDialog
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import java.util.*


/**
 * @author: cgy
 * @description: 添加待办清单
 * @date: 2020/01/06 13:54
 */
class AddTodoActivity : BaseActivity<AddTodoPresenter>(), AddTodoContract.View {

    var todoResponse: TodoResponse? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerAddTodoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addTodoModule(AddTodoModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_add_todo //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        intent.run {
            todoResponse = getSerializableExtra("data") as TodoResponse?
        }
        toolbar.run {
            setSupportActionBar(this)
            title = "添加待办清单"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        if (todoResponse == null) {
            color_view_add_todo.setView(TodoType.TodoType1.color)
            tv_add_todo_prox.text = TodoType.TodoType1.content
        } else {
            toolbar.title = "编辑待办清单"
            todoResponse?.let {
                et_add_todo_title.setText(it.title)
                et_add_todo_content.setText(it.content)
                tv_add_todo_date.text = it.dateStr
                color_view_add_todo.setView(TodoType.byType(it.priority).color)
                tv_add_todo_prox.text = TodoType.byType(it.priority).content
            }
        }
        SettingUtil.setShapeColor(tv_add_todo_submit, SettingUtil.getColor(this))
    }

    @OnClick(R.id.tv_add_todo_date, R.id.ll_add_todo_prox, R.id.tv_add_todo_submit)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.tv_add_todo_date -> {
                MaterialDialog(this).show {
                    cornerRadius(0f)
                    datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                        this@AddTodoActivity.tv_add_todo_date.text = DateTimeUtil.formatDate(date.time, DateTimeUtil.DATE_PATTERN)

                    }
                }
            }
            R.id.ll_add_todo_prox -> {
                PriorityDialog(this, TodoType.byValue(this@AddTodoActivity.tv_add_todo_prox.text.toString()).type).apply {
                    setPriorityInterface(object : PriorityDialog.PriorityInterface {
                        override fun onSelect(type: TodoType) {
                            this@AddTodoActivity.tv_add_todo_prox.text = type.content
                            this@AddTodoActivity.color_view_add_todo.setView(type.color)
                        }
                    })
                }.show()
            }
            R.id.tv_add_todo_submit -> {
               if (TextUtils.isEmpty(et_add_todo_title.text.toString())) {
                   showMessage("请填写标题")
               } else if (TextUtils.isEmpty(et_add_todo_content.text.toString())) {
                   showMessage("请填写内容")
               } else if (TextUtils.isEmpty(tv_add_todo_date.text.toString())) {
                   showMessage("请填写预计完成时间")
               } else {
                   if (todoResponse == null) {
                       MaterialDialog(this).show {
                           title(R.string.title)
                           message(text = "确定要添加吗？")
                           positiveButton(text = "添加") {
                               mPresenter?.addTodo(this@AddTodoActivity.et_add_todo_title.text.toString(),
                                       this@AddTodoActivity.et_add_todo_content.toString(),
                                       this@AddTodoActivity.tv_add_todo_date.text.toString(),
                                       TodoType.byValue(this@AddTodoActivity.tv_add_todo_prox.text.toString()).type)
                           }
                           negativeButton(R.string.cancel)
                       }
                   } else {
                       MaterialDialog(this).show {
                           title(R.string.title)
                           message(text = "确定要提交编辑吗？")
                           positiveButton(text = "提交") {
                               mPresenter?.updateTodo(this@AddTodoActivity.et_add_todo_title.text.toString(),
                                       this@AddTodoActivity.et_add_todo_content.text.toString(),
                                       this@AddTodoActivity.tv_add_todo_date.text.toString(),
                                       TodoType.byValue(this@AddTodoActivity.tv_add_todo_prox.text.toString()).type,
                                       todoResponse!!.id)
                           }
                           negativeButton(R.string.cancel)
                       }
                   }
               }
            }

        }
    }

    override fun addTodoSuccess() {
        AddTodoEvent().post()
        finish()
    }

    override fun addTodoFailed(errorMsg: String) {
        showMessage(errorMsg)
    }


}
