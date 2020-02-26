package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.AddTodoModule

import com.jess.arms.di.scope.ActivityScope
import com.cgy.wandroid.ui.todo.AddTodoActivity


@ActivityScope
@Component(modules = arrayOf(AddTodoModule::class), dependencies = arrayOf(AppComponent::class))
interface AddTodoComponent {
    fun inject(activity: AddTodoActivity)
}
