package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.TodoContract
import com.cgy.wandroid.mvp.model.todo.TodoModel


@Module
//构建TodoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TodoModule(private val view: TodoContract.View) {
    @ActivityScope
    @Provides
    fun provideTodoView(): TodoContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideTodoModel(model: TodoModel): TodoContract.Model {
        return model
    }
}
