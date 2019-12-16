package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.publicChildContract
import com.cgy.wandroid.mvp.model.publicChildModel


@Module
//构建publicChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class publicChildModule(private val view: publicChildContract.View) {
    @FragmentScope
    @Provides
    fun providepublicChildView(): publicChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun providepublicChildModel(model: publicChildModel): publicChildContract.Model {
        return model
    }
}
