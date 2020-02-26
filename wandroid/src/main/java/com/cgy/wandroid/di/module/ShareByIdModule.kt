package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.ShareByIdContract
import com.cgy.wandroid.mvp.model.share.ShareByIdModel


@Module
//构建ShareByIdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ShareByIdModule(private val view: ShareByIdContract.View) {
    @ActivityScope
    @Provides
    fun provideShareByIdView(): ShareByIdContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideShareByIdModel(model: ShareByIdModel): ShareByIdContract.Model {
        return model
    }
}
