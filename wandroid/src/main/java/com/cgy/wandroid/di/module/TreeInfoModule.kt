package com.cgy.wandroid.di.module

import com.cgy.wandroid.mvp.contract.TreeInfoContract
import com.cgy.wandroid.mvp.model.main.tree.TreeInfoModel
import com.jess.arms.di.scope.FragmentScope
import dagger.Module
import dagger.Provides


@Module
//构建TreeInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TreeInfoModule(private val view: TreeInfoContract.View) {
    @FragmentScope
    @Provides
    fun provideTreeInfoView(): TreeInfoContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideTreeInfoModel(model: TreeInfoModel): TreeInfoContract.Model {
        return model
    }
}
