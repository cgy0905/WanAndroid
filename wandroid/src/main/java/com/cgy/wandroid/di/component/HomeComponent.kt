package com.cgy.wandroid.di.component




import com.cgy.wandroid.di.module.HomeModule
import com.cgy.wandroid.ui.main.home.HomeFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import dagger.Component

/**
 * ================================================
 * Description:
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */

@FragmentScope
@Component(modules = arrayOf(HomeModule::class), dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
    fun inject(fragment: HomeFragment)
}
