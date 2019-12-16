package com.cgy.wandroid.ui.main.home.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.cgy.wandr.SearchPresenter

import com.cgy.wandroid.base.BaseActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.utils.ArmsUtils

import com.cgy.wandroid.di.component.DaggerSearchComponent
import com.cgy.wandroid.di.module.SearchModule
import com.cgy.wandroid.mvp.contract.SearchContract
import com.cgy.wandroid.mvp.presenter.SearchPresenter

import com.cgy.wandroid.R
import com.cgy.wandroid.mvp.model.entity.SearchResponse
import com.cgy.wandroid.ui.main.home.adapter.SearchHistoryAdapter
import com.cgy.wandroid.util.CacheUtil
import com.cgy.wandroid.util.ColorUtils
import com.cgy.wandroid.util.SettingUtil
import com.cgy.wandroid.util.ShowUtil
import com.google.gson.Gson
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.flow_layout.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @author: cgy
 * @description:搜索
 * @date: 2019/09/20 14:44
 */
class SearchActivity : BaseActivity<SearchPresenter>(), SearchContract.View {
    var mtagData = mutableListOf<SearchResponse>()//搜索热词数据
    var historyData = mutableListOf<String>()//搜索历史数据
    lateinit var adapter : SearchHistoryAdapter//搜索历史适配器

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchModule(SearchModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_search //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                //返回的时候关闭当前界面输入法
                ShowUtil.hideSoftKeyboard(this@SearchActivity)
                finish()
            }
        }
        search_text1.setTextColor(SettingUtil.getColor(this))
        search_text2.setTextColor(SettingUtil.getColor(this))
        search_clear.setOnClickListener {
            MaterialDialog(this).show {
                title(text = "温馨提示")
                message(text = "确定清空搜索历史吗？")
                positiveButton(text = "清空"){
                    historyData.clear()
                    adapter.setNewData(historyData)
                    CacheUtil.setSearchHistoryData(Gson().toJson(historyData))
                }
            }
        }
        search_flow_layout.run {
            setOnTagClickListener { view, position, parent ->
                val name = mtagData[position].name
                if (historyData.contains(name)) {
                    //当搜索历史中包含该数据时 删除添加
                    historyData.remove(name)
                } else if (historyData.size >= 10) {
                    historyData.removeAt(historyData.size - 1)
                }
                historyData.add(0, name)
                this@SearchActivity.adapter.setNewData(historyData)
                CacheUtil.setSearchHistoryData(Gson().toJson(historyData))
                launchActivity(Intent(this@SearchActivity, SearchResultActivity::class.java).apply {
                    putExtra("searchKey", name)
                })
                false

            }
        }
        historyData = CacheUtil.getSearchHistoryData()
        adapter = SearchHistoryAdapter(historyData).apply {
            //设置空布局
            emptyView = LayoutInflater.from(this@SearchActivity).inflate(R.layout.search_empty_view, null)
            //删除单个搜索历史
            setOnItemChildClickListener { adapter, view, position ->
                adapter.remove(position)
                CacheUtil.setSearchHistoryData(Gson().toJson(historyData))
            }
            //点击了搜索历史的某一个
            setOnItemClickListener { adapter, view, position ->
                launchActivity(Intent(this@SearchActivity, SearchResultActivity::class.java).apply {
                    putExtra("searchKey", historyData[position])
                })
            }
        }
        search_recycler_view.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
            adapter = this@SearchActivity.adapter
            isNestedScrollingEnabled = false
        }
        mPresenter?.getHotData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.run {
            maxWidth = Integer.MAX_VALUE
            onActionViewExpanded()
            queryHint = "请输入关键字搜索"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //searchView的监听
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //当点击搜索时,输入法的搜索和右边的搜索都会触发
                    query?.let {
                        if (historyData.contains(it)) {
                            //当搜索历史中包含该数据时 删除
                            historyData.remove(it)
                        } else if (historyData.size >= 10) {
                            //如果集合的size有10个以上了,删除最后一个
                            historyData.removeAt(historyData.size - 1)
                        }
                        launchActivity(Intent(this@SearchActivity, SearchResultActivity::class.java).apply {
                            putExtra("searchKey", it)
                        })
                        historyData.add(0, it)//添加新数据到第一条
                        this@SearchActivity.adapter.setNewData(historyData)//刷新适配器
                        CacheUtil.setSearchHistoryData(Gson().toJson(historyData))//保存到本地

                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
            isSubmitButtonEnabled = true //右边是否展示搜索图标
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_search)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 获取搜索热词成功
     */
    override fun requestSearchSuccess(tagData: MutableList<SearchResponse>) {
        mtagData.addAll(tagData)
        search_flow_layout.adapter = object : TagAdapter<SearchResponse>(mtagData) {
            override fun getView(parent: FlowLayout?, position: Int, hotSearchBean: SearchResponse?): View {
                return LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout, search_flow_layout, false)
                        .apply {
                            flow_tag.text = hotSearchBean?.name
                            flow_tag.setTextColor(ColorUtils.randomColor())
                        }
            }
        }
    }
}
