package com.cgy.wandroid.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import butterknife.OnClick
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseActivity
import com.cgy.wandroid.di.component.DaggerLoginComponent
import com.cgy.wandroid.di.module.LoginModule
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.mvp.contract.LoginContract
import com.cgy.wandroid.mvp.model.entity.UserInfoResponse
import com.cgy.wandroid.mvp.presenter.LoginPresenter
import com.cgy.wandroid.util.CacheUtil
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.LoadingDialog
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author: cgy
 * @description:登录页面
 * 2019/9/16 17:20
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent.builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .build()
                .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    /**
     * 初始化界面操作
     */
    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "登录"
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener {
                finish()
            }
        }
        SettingUtil.setShapeColor(tv_login, SettingUtil.getColor(this))
        tv_register?.setTextColor(SettingUtil.getColor(this))
        et_username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    iv_clear.visibility = View.VISIBLE
                } else {
                    iv_clear.visibility = View.GONE
                }
            }

        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    cb_pwd_visible.visibility = View.VISIBLE
                } else {
                    cb_pwd_visible.visibility = View.GONE
                }
            }

        })
        cb_pwd_visible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                et_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            et_password.setSelection(et_password.text.toString().length)
        }
    }

    @OnClick(R.id.iv_clear, R.id.tv_login, R.id.tv_register)
    fun onViewClicked(view: View) {
        when(view.id) {
            R.id.iv_clear -> et_username.setText("")
            R.id.tv_login -> {
                when {
                    et_username.text.isEmpty() -> showMessage("请填写账号")
                    et_password.text.isEmpty() -> showMessage("请填写密码")
                    else -> mPresenter?.login(et_username.text.toString(), et_password.text.toString())
                }
            }
            R.id.tv_register -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    override fun onSuccess(userInfo: UserInfoResponse) {
        CacheUtil.setUser(userInfo)//保存账号信息
        //保存账号与密码,在其他接口请求的时候当做Cookie传到Header中
        LoginFreshEvent(true, userInfo.collectIds).post()//通知其他界面登录成功了,有收藏的地方需要刷新数据
        finish()
    }

    override fun showProgress() {
        LoadingDialog.show(this)
    }

    override fun closeProgress() {
        LoadingDialog.dismiss()
    }
}