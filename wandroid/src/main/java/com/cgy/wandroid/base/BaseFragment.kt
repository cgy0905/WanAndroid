package com.cgy.wandroid.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cgy.wandroid.util.ShowUtil
import com.jess.arms.base.delegate.IFragment
import com.jess.arms.integration.cache.Cache
import com.jess.arms.integration.cache.CacheType
import com.jess.arms.integration.lifecycle.FragmentLifecycleable
import com.jess.arms.mvp.IPresenter
import com.jess.arms.mvp.IView
import com.jess.arms.utils.ArmsUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import me.yokeyword.fragmentation.SupportFragment
import javax.inject.Inject

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/18 11:42
 */
abstract class BaseFragment<P : IPresenter> : SupportFragment(), IFragment, FragmentLifecycleable, IView{
    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    private var mCache: Cache<*, *>? = null
    protected var mContext: Context? = null
    @Inject
    @JvmField
    var mPresenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
       if (mCache == null) {
           mCache = ArmsUtils.obtainAppComponentFromContext(activity).cacheFactory().build(CacheType.ACTIVITY_CACHE)
       }
        return mCache as Cache<String, Any>
    }

    override fun provideLifecycleSubject(): Subject<FragmentEvent> {
        return mLifecycleSubject
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.run {
            onDestroy()//释放资源
            null
        }
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    /**
     * 是否使用 EventBus 默认true
     */
    override fun useEventBus(): Boolean {
        return true
    }

    override fun setData(data: Any?) {
        //MVPArms的 fragment通信方法，使用event替代了，这个就不要了
    }

    override fun initData(savedInstanceState: Bundle?) {
        //这是MvpArms的初始化数据方法，fragment只要创建就会执行，没有懒加载效果，我已经用了其他的库替代了，所以这个方法继承后我们可以 实现或不实现都阔以
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun showLoading() {
        ShowUtil.showLoading(_mActivity)
    }

    override fun hideLoading() {
        ShowUtil.dismissLoading()
    }
    override fun showMessage(message: String) {
        ShowUtil.showDialog(_mActivity,message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }
}