package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

<<<<<<< HEAD
<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/component/HomeComponent.kt
import com.cgy.wandroid.di.module.HomeModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.main.home.HomeFragment
=======
import com.cgy.wandroid.di.module.MainModule

import com.jess.arms.di.scope.ActivityScope
import com.cgy.wandroid.ui.MainActivity
import com.cgy.wandroid.ui.fragment.MainFragment
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/component/MainComponent.kt
=======
import com.cgy.wandroid.di.module.HomeModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.home.HomeFragment
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4


/**
 * ================================================
 * Description:
 * <p>
<<<<<<< HEAD
<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/component/HomeComponent.kt
 * Created by MVPArmsTemplate on 09/19/2019 15:00
=======
 * Created by MVPArmsTemplate on 09/19/2019 14:01
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/component/MainComponent.kt
=======
 * Created by MVPArmsTemplate on 09/19/2019 15:00
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
<<<<<<< HEAD
<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/component/HomeComponent.kt
=======
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
@FragmentScope
@Component(modules = arrayOf(HomeModule::class), dependencies = arrayOf(AppComponent::class))
interface HomeComponent {
    fun inject(fragment: HomeFragment)
<<<<<<< HEAD
=======
@ActivityScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: MainFragment)
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/component/MainComponent.kt
=======
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
}
