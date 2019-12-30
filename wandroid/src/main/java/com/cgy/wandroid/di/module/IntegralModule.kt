package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.IntegralContract
import com.cgy.wandroid.mvp.model.integral.IntegralModel


@Module
//构建IntegralModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class IntegralModule(private val view: IntegralContract.View) {
    @ActivityScope
    @Provides
    fun provideIntegralView(): IntegralContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideIntegralModel(model: IntegralModel): IntegralContract.Model {
        return model
    }
}
