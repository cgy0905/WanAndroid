package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.TodoModule

import com.jess.arms.di.scope.ActivityScope
import com.cgy.wandroid.ui.todo.TodoActivity


@ActivityScope
@Component(modules = arrayOf(TodoModule::class), dependencies = arrayOf(AppComponent::class))
interface TodoComponent {
    fun inject(activity: TodoActivity)
}
