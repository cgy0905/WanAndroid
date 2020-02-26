package com.cgy.wandroid.ui.main.mine

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerMineComponent
import com.cgy.wandroid.di.module.MineModule
import com.cgy.wandroid.event.LoginFreshEvent
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.contract.MineContract
import com.cgy.wandroid.mvp.model.entity.IntegralResponse
import com.cgy.wandroid.mvp.model.entity.UserInfoResponse
import com.cgy.wandroid.mvp.presenter.MinePresenter
import com.cgy.wandroid.ui.collect.CollectActivity
import com.cgy.wandroid.ui.integral.IntegralActivity
import com.cgy.wandroid.ui.login.LoginActivity
import com.cgy.wandroid.ui.setting.SettingActivity
import com.cgy.wandroid.ui.todo.TodoActivity
import com.cgy.wandroid.util.CacheUtil
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.util.ShowUtil
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description: 我的页面
 * @date: 2019/09/20 11:33
 */
class MineFragment : BaseFragment<MinePresenter>(), MineContract.View {

    private lateinit var userInfo : UserInfoResponse
    var integral : IntegralResponse? = null

    companion object {
        fun newInstance(): MineFragment {
            return MineFragment()
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMineComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mineModule(MineModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            title = "我的"
        }
        mine_refresh_layout.run {
            setOnRefreshListener {
                //刷新积分
                mPresenter?.getIntegral()
            }
        }
        mine_refresh_layout.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        toolbar.setBackgroundColor(SettingUtil.getColor(_mActivity))
        linear_mine.setBackgroundColor(SettingUtil.getColor(_mActivity))
        tv_mine_integral.setTextColor(SettingUtil.getColor(_mActivity))
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (CacheUtil.isLogin()) {
            //如果登录了 赋值,并且请求积分接口
            userInfo = CacheUtil.getUser()
            tv_username.text = userInfo.username
            mine_refresh_layout.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //未登录 不请求积分接口
            tv_username.text = "去登录"
            tv_mine_integral.text = "0"
        }
    }

    /**
     * 获取积分成功的回调
     */
    override fun getIntegralSuccess(integral: IntegralResponse) {
        this.integral = integral
        mine_refresh_layout.isRefreshing = false
        tv_mine_integral.text = integral.coinCount.toString()
    }

    /**
     * 获取积分失败回调
     */
    override fun getIntegralFailed(errorMsg: String) {
        mine_refresh_layout.isRefreshing = false
        ShowUtil.showToast(_mActivity, errorMsg)
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @Subscribe
    fun freshLogin(event : LoginFreshEvent) {
        if (event.login) {
            //接收到登录了, 赋值 并去请求积分接口
            userInfo = CacheUtil.getUser()
            tv_username.text = userInfo.username
            //调起请求 设置触发 下拉swipe
            mine_refresh_layout.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //接收到退出登录了,清空赋值
            tv_username.text = "去登录"
            tv_mine_integral.text = "0"
        }
    }

    /**
     * 接收到event时,重新设置当前界面控件的主题颜色和一些其他配置
     */
    fun settingEvent(event : SettingChangeEvent) {
        mine_refresh_layout.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        toolbar.setBackgroundColor(SettingUtil.getColor(_mActivity))
        linear_mine.setBackgroundColor(SettingUtil.getColor(_mActivity))
        tv_mine_integral.setTextColor(SettingUtil.getColor(_mActivity))
    }

    @OnClick(R.id.ll_setting, R.id.ll_mine_collect, R.id.linear_mine, R.id.ll_mine_todo, R.id.ll_mine_integral)
    fun onViewClicked(view: View) {
        when(view.id) {
            R.id.linear_mine -> {
                if (CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                }
            }
            R.id.ll_mine_collect -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                } else {
                    launchActivity(Intent(_mActivity, CollectActivity::class.java))
                }
            }
            R.id.ll_mine_todo -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                } else {
                    launchActivity(Intent(_mActivity, TodoActivity::class.java))
                }
            }
            R.id.ll_mine_integral -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                } else {
                    launchActivity(Intent(_mActivity, IntegralActivity::class.java).apply {
                        integral?.let {
                            putExtras(Bundle().apply {
                                putSerializable("integral", it)
                            })
                        }
                    })
                }
            }
            R.id.ll_setting -> {
                launchActivity(Intent(_mActivity, SettingActivity::class.java))
            }
        }

    }

}
