package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.NavigationModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.main.tree.NavigationFragment


@FragmentScope
@Component(modules = arrayOf(NavigationModule::class), dependencies = arrayOf(AppComponent::class))
interface NavigationComponent {
    fun inject(fragment: NavigationFragment)
}
