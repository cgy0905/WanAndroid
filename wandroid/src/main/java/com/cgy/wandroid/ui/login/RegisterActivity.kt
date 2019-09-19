package com.cgy.wandroid.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import butterknife.OnClick
import com.blankj.utilcode.util.ToastUtils
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
import com.jess.arms.integration.AppManager
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 * @author: cgy
 * @description:注册界面
 * @date: 2019/9/16 17:20
 */
class RegisterActivity : BaseActivity<LoginPresenter>(), LoginContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .build()
                .injectRegister(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_register //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "注册"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        SettingUtil.setShapeColor(tv_register, SettingUtil.getColor(this))
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
        et_password.addTextChangedListener(object : TextWatcher{
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
        et_confirm_pwd.addTextChangedListener(object : TextWatcher{
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
        cb_pwd_visible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                et_password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            et_password.setSelection(et_password.text.toString().length)
        }
        cb_confirm_pwd_visible.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                et_confirm_pwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                et_confirm_pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            et_confirm_pwd.setSelection(et_confirm_pwd.text.toString().length)
        }
    }

    @OnClick(R.id.iv_clear, R.id.tv_register)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.iv_clear -> et_username.setText("")
            R.id.tv_register -> {
                when {
                    et_username.text.isEmpty() -> showMessage("请填写账号")
                    et_username.text.length < 6 -> showMessage("账号长度不能小于6位")
                    et_password.text.isEmpty() -> showMessage("请填写密码")
                    et_password.text.length < 6 -> showMessage("密码长度不能小于6位")
                    et_confirm_pwd.text.isEmpty() -> showMessage("请填写确认密码")
                    et_confirm_pwd.text.toString() != et_password.text.toString() -> showMessage("密码不一致")
                    else -> mPresenter?.register(et_username.text.toString(), et_password.text.toString(), et_confirm_pwd.text.toString())
                }
            }
        }
    }

    override fun onSuccess(userInfo: UserInfoResponse) {
        CacheUtil.setUser(userInfo)//保存账户信息
        AppManager.getAppManager().killActivity(LoginActivity::class.java)
        LoginFreshEvent(true, userInfo.collectIds).post()
        finish()
    }

    override fun showProgress() {
        LoadingDialog.show(this)
    }

    override fun closeProgress() {
        LoadingDialog.dismiss()
    }

    override fun showMessage(message: String) {
        if (TextUtils.isEmpty(message)) {
            return
        }
        ToastUtils.showShort(message)
    }


}
