package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.SystemContract
import com.cgy.wandroid.mvp.model.main.tree.SystemModel


@Module
//构建SystemModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SystemModule(private val view: SystemContract.View) {
    @FragmentScope
    @Provides
    fun provideSystemView(): SystemContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideSystemModel(model: SystemModel): SystemContract.Model {
        return model
    }
}
