package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.HomeContract
import com.cgy.wandroid.mvp.model.main.home.HomeModel

/**
 * Description
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module

//构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class HomeModule(private val view: HomeContract.View) {
    @FragmentScope
    @Provides
    fun provideHomeView(): HomeContract.View {
        return this.view
    }


    @FragmentScope
    @Provides
    fun provideHomeModel(model: HomeModel): HomeContract.Model {

        return model
    }
}
