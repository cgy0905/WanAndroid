package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.NavigationContract
import com.cgy.wandroid.mvp.model.main.tree.NavigationModel


@Module
//构建NavigationModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class NavigationModule(private val view: NavigationContract.View) {
    @FragmentScope
    @Provides
    fun provideNavigationView(): NavigationContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideNavigationModel(model: NavigationModel): NavigationContract.Model {
        return model
    }
}
