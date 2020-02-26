package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.SystemModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.main.tree.SystemFragment


@FragmentScope
@Component(modules = arrayOf(SystemModule::class), dependencies = arrayOf(AppComponent::class))
interface SystemComponent {
    fun inject(fragment: SystemFragment)
}
