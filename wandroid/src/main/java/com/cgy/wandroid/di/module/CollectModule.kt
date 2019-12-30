package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.CollectContract
import com.cgy.wandroid.mvp.model.collect.CollectModel


@Module
//构建CollectArticleModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class CollectModule(private val view: CollectContract.View) {
    @FragmentScope
    @Provides
    fun provideCollectArticleView(): CollectContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideCollectArticleModel(model: CollectModel): CollectContract.Model {
        return model
    }
}
