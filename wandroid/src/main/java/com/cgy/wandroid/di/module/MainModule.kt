package com.cgy.wandroid.di.module

<<<<<<< HEAD
import com.cgy.wandroid.mvp.contract.MainContract
import com.cgy.wandroid.mvp.model.MainModel
import com.jess.arms.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

=======
import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/module/HomeModule.kt
import com.cgy.wandroid.mvp.contract.HomeContract
import com.cgy.wandroid.mvp.model.HomeModel
=======
import com.cgy.wandroid.mvp.contract.MainContract
import com.cgy.wandroid.mvp.model.MainModel
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/module/MainModule.kt

>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4

/**
 * ================================================
 * Description:
 * <p>
<<<<<<< HEAD
 * Created by MVPArmsTemplate on 09/19/2019 14:01
=======
<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/module/HomeModule.kt
 * Created by MVPArmsTemplate on 09/19/2019 15:00
=======
 * Created by MVPArmsTemplate on 09/19/2019 14:01
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/module/MainModule.kt
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
<<<<<<< HEAD
//构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MainModule(private val view: MainContract.View) {
    @FragmentScope
    @Provides
    fun provideMainView(): MainContract.View {
=======
<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/module/HomeModule.kt
//构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class HomeModule(private val view: HomeContract.View) {
    @FragmentScope
    @Provides
    fun provideHomeView(): HomeContract.View {
=======
//构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MainModule(private val view: MainContract.View) {
    @ActivityScope
    @Provides
    fun provideMainView(): MainContract.View {
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/module/MainModule.kt
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
        return this.view
    }

    @FragmentScope
    @Provides
<<<<<<< HEAD
    fun provideMainModel(model: MainModel): MainContract.Model {
=======
<<<<<<< HEAD:wandroid/src/main/java/com/cgy/wandroid/di/module/HomeModule.kt
    fun provideHomeModel(model: HomeModel): HomeContract.Model {
=======
    fun provideMainModel(model: MainModel): MainContract.Model {
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4:wandroid/src/main/java/com/cgy/wandroid/di/module/MainModule.kt
>>>>>>> 1bc58334706fab8674aff54e0b74facf453229f4
        return model
    }
}
