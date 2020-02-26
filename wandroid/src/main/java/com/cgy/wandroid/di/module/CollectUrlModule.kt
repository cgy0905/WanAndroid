package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.CollectUrlContract
import com.cgy.wandroid.mvp.model.collect.CollectUrlModel


@Module
//构建CollectUrlModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class CollectUrlModule(private val view: CollectUrlContract.View) {
    @FragmentScope
    @Provides
    fun provideCollectUrlView(): CollectUrlContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideCollectUrlModel(model: CollectUrlModel): CollectUrlContract.Model {
        return model
    }
}
