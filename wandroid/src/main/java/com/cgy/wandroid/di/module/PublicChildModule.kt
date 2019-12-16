package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.PublicChildContract
import com.cgy.wandroid.mvp.model.main.pub.PublicChildModel


@Module
//构建publicChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class PublicChildModule(private val view: PublicChildContract.View) {
    @FragmentScope
    @Provides
    fun providepublicChildView(): PublicChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun providepublicChildModel(model: PublicChildModel): PublicChildContract.Model {
        return model
    }
}
