package com.cgy.wandroid.ui.main.pub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.di.component.DaggerPublicComponent
import com.cgy.wandroid.di.module.PublicModule
import com.cgy.wandroid.mvp.contract.PublicContract
import com.cgy.wandroid.mvp.presenter.PublicPresenter
import com.jess.arms.di.component.AppComponent


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/20/2019 11:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
/**
 * @author: cgy
 * @description:
 * @date: 2019/09/20 11:31
 */
class PublicFragment : BaseFragment<PublicPresenter>(), PublicContract.View {
    companion object {
        fun newInstance(): PublicFragment {
            val fragment = PublicFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerPublicComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .publicModule(PublicModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_public, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }


}
