package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.CollectModule

import com.jess.arms.di.scope.FragmentScope
import com.cgy.wandroid.ui.collect.CollectArticleFragment


@FragmentScope
@Component(modules = arrayOf(CollectModule::class), dependencies = arrayOf(AppComponent::class))
interface CollectComponent {
    fun inject(fragment: CollectArticleFragment)
}
