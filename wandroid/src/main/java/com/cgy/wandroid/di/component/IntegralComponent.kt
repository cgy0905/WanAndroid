package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.IntegralModule

import com.jess.arms.di.scope.ActivityScope
import com.cgy.wandroid.ui.integral.IntegralActivity


@ActivityScope
@Component(modules = arrayOf(IntegralModule::class), dependencies = arrayOf(AppComponent::class))
interface IntegralComponent {
    fun inject(activity: IntegralActivity)
}
