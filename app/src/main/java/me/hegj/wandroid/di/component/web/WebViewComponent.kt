package me.hegj.wandroid.di.component.web

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component
import me.hegj.wandroid.di.module.web.WebviewModule
import me.hegj.wandroid.mvp.ui.activity.web.WebViewActivity


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/10/2019 09:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = arrayOf(WebviewModule::class), dependencies = arrayOf(AppComponent::class))
interface WebViewComponent {
    fun inject(activity: WebViewActivity)
}
