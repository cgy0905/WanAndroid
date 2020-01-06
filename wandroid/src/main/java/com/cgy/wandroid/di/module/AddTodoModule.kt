package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.AddTodoContract
import com.cgy.wandroid.mvp.model.todo.AddTodoModel


@Module
//构建AddTodoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class AddTodoModule(private val view: AddTodoContract.View) {
    @ActivityScope
    @Provides
    fun provideAddTodoView(): AddTodoContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideAddTodoModel(model: AddTodoModel): AddTodoContract.Model {
        return model
    }
}
