package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.SearchResultModule

import com.jess.arms.di.scope.ActivityScope
import com.cgy.wandroid.ui.main.home.search.SearchResultActivity


@ActivityScope
@Component(modules = arrayOf(SearchResultModule::class),dependencies = arrayOf(AppComponent::class))
interface SearchResultComponent {
    fun inject(activity:SearchResultActivity)
}
