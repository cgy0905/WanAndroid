package com.cgy.wandroid.ui.main.project

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
import com.cgy.wandroid.di.component.DaggerProjectComponent
import com.cgy.wandroid.di.module.ProjectModule
import com.cgy.wandroid.event.SettingChangeEvent
import com.cgy.wandroid.mvp.contract.ProjectContract
import com.cgy.wandroid.mvp.model.entity.ClassifyResponse
import com.cgy.wandroid.mvp.presenter.ProjectPresenter
import com.cgy.wandroid.ui.main.project.fragment.ProjectChildFragment
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.weight.ScaleTransitionPagerTitleView
import com.cgy.wandroid.weight.loadCallback.ErrorCallback
import com.cgy.wandroid.weight.loadCallback.LoadingCallback
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
 * @description:
 * @date: 2019/09/19 15:19
 */
class ProjectFragment : BaseFragment<ProjectPresenter>(), ProjectContract.View {

    var mDataList : MutableList<ClassifyResponse> = mutableListOf()
    var fragments : MutableList<SupportFragment> = mutableListOf()
    internal var papgerAdapter : ViewPagerAdapter? = null
    lateinit var loadSir : LoadService<Any>


    companion object {
        fun newInstance(): ProjectFragment {
            val fragment = ProjectFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .projectModule(ProjectModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       val rootView =  inflater.inflate(R.layout.fragment_project, container, false)
        //绑定loadSir
        loadSir = LoadSir.getDefault().register(rootView.findViewById(R.id.view_pager)) {
            loadSir.showCallback(LoadingCallback::class.java)
            mPresenter?.getProjectTitles()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return rootView
    }

    override fun initData(savedInstanceState: Bundle?) {
        linear_viewpager.setBackgroundColor(SettingUtil.getColor(_mActivity))
        mPresenter?.getProjectTitles()//初始化的时候请求数据

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        papgerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        view_pager.adapter = papgerAdapter
        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mDataList[index].name
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener { view_pager.setCurrentItem(index, false) }
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
                    roundRadius = UIUtil.dip2px(context, 6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator  = DecelerateInterpolator(2.0f)
                    setColors(Color.WHITE)
                }
            }
        }
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, view_pager)
    }

    /**
     * 获取项目头部标题成功
     */
    override fun requestTitleSuccess(titles: MutableList<ClassifyResponse>) {
        //请求到 项目分类头部标题集合
        if (titles.size == 0) {
            //没有数据，说明肯定出错了 这种情况只有第一次会出现，因为第一次请求成功后，会做本地保存操作，下次就算请求失败了，也会从本地取出缓存
            loadSir.showCallback(ErrorCallback::class.java)
        } else {
            loadSir.showSuccess()
            this.mDataList = titles
            if (fragments.size == 0) {
                //防止重复添加
                //根据头部集合循环添加对应的fragment
                for (i in titles.indices) {
                    fragments.add(ProjectChildFragment.newInstance(titles[i].id, 1)) //分类项目页码从1开始
                }
                //在第一次添加 最新项目fragment
                this.mDataList.add(0, ClassifyResponse(arrayListOf(), 0, 0, "最新项目", 0, 0, false, 0))
                fragments.add(0, ProjectChildFragment.newInstance(true, 0))//最新项目页码从0开始
            }
            //如果viewpager和magicIndicator不为空的话,刷新它们 为空的话 说明用户还没有进来这个fragment
            papgerAdapter?.notifyDataSetChanged()
            magic_indicator?.navigator?.notifyDataSetChanged()
            view_pager?.offscreenPageLimit = fragments.size
        }
    }

    /**
     * 接收到eventsh时,重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event : SettingChangeEvent) {
        linear_viewpager.setBackgroundColor(SettingUtil.getColor(_mActivity))
        SettingUtil.setLoadingColor(_mActivity, loadSir)
    }


}
