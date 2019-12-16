package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.cgy.wandroid.mvp.model.SearchResultModel


@Module
 //构建SearchResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SearchResultModule(private val view:SearchResultContract.View) {
    @ActivityScope
    @Provides
    fun provideSearchResultView():SearchResultContract.View{
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideSearchResultModel(model:SearchResultModel):SearchResultContract.Model{
        return model
    }
}
