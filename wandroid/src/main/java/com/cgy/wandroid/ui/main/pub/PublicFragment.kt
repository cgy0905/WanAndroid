package com.cgy.wandroid.ui.main.pub

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.cgy.wandroid.R
import com.cgy.wandroid.base.BaseFragment
import com.cgy.wandroid.base.ViewPagerAdapter
import com.cgy.wandroid.di.component.DaggerPublicComponent
import com.cgy.wandroid.di.module.PublicModule
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.contract.PublicContract
import com.cgy.wandroid.mvp.model.entity.ClassifyResponse
import com.cgy.wandroid.mvp.presenter.PublicPresenter
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.ScaleTransitionPagerTitleView
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.include_viewpager.*
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.Subscribe


/**
 * @author: cgy
 * @description:公众号
 * @date: 2019/09/20 11:31
 */
class PublicFragment : BaseFragment<PublicPresenter>(), PublicContract.View {

    var mDataList : MutableList<ClassifyResponse> = mutableListOf()
    var fragments : MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter : ViewPagerAdapter? = null
    lateinit var loadSir : LoadService<Any>

    companion object {
        fun newInstance(): PublicFragment {
            return PublicFragment()
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

        val rootView = inflater.inflate(R.layout.fragment_viewpager, container, false)
        //绑定loadsir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.view_pager)) {
            mPresenter?.getProjectTitles()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }

        return rootView
    }

    override fun initData(savedInstanceState: Bundle?) {
       super.initData(savedInstanceState)
        linear_viewpager.setBackgroundColor(SettingUtil.getColor(_mActivity))
        mPresenter?.getProjectTitles()//初始化的时候请求数据
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        view_pager.adapter = pagerAdapter
        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mDataList[index].name
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener {
                        view_pager.setCurrentItem(index, false)
                    }
                }
            }

            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                    roundRadius = UIUtil.dip2px(context,6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    setColors(Color.WHITE)
                }
            }

        }
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, view_pager)
    }

    override fun requesTitleSuccess(titles: MutableList<ClassifyResponse>) {
        //请求到 项目分类头部标题集合
        if (titles.size == 0) {
            //没有数据，说明肯定出错了 这种情况只有第一次会出现，因为第一次请求成功后，会做本地保存操作，下次就算请求失败了，也会从本地取出来
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            loadSir.showSuccess()
            this.mDataList = titles
            if (fragments.size == 0) {
                //根据头部集合循环添加对应的Fragment
                for (i in titles.indices) {
                    fragments.add(PublicChildFragment.newInstance(titles[i].id))
                }
            }
            //如果viewpager和magicindicator不为空的hua,刷新它们,为空的话说明 用户还没有进来这个fragment
            pagerAdapter?.notifyDataSetChanged()
            magic_indicator?.navigator?.notifyDataSetChanged()
            view_pager?.offscreenPageLimit = fragments.size
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event : SettingChangeEvent) {
        linear_viewpager.setBackgroundColor(SettingUtil.getColor(_mActivity))
        SettingUtil.setLoadingColor(_mActivity, loadSir)
    }


}
