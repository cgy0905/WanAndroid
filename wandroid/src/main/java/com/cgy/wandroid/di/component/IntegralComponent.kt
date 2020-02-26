package com.cgy.wandroid.di.component

import com.cgy.wandroid.di.module.IntegralModule
import com.cgy.wandroid.ui.integral.IntegralActivity
import com.cgy.wandroid.ui.integral.IntegralHistoryActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component


@ActivityScope
@Component(modules = arrayOf(IntegralModule::class), dependencies = arrayOf(AppComponent::class))
interface IntegralComponent {
    fun inject(activity: IntegralActivity)
    fun inject(activity : IntegralHistoryActivity)
}
