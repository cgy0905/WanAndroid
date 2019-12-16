package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.publicChildModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.main.pub.publicChildFragment


@FragmentScope
@Component(modules = arrayOf(publicChildModule::class), dependencies = arrayOf(AppComponent::class))
interface publicChildComponent {
    fun inject(fragment: publicChildFragment)
}
