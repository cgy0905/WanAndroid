package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.CollectUrlModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.collect.CollectUrlFragment


@FragmentScope
@Component(modules = arrayOf(CollectUrlModule::class), dependencies = arrayOf(AppComponent::class))
interface CollectUrlComponent {
    fun inject(fragment: CollectUrlFragment)
}
