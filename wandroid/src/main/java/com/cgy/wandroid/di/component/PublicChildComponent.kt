package com.cgy.wandroid.di.component

import com.cgy.wandroid.di.module.PublicChildModule
import com.cgy.wandroid.ui.main.pub.PublicChildFragment
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.FragmentScope
import dagger.Component


@FragmentScope
@Component(modules = arrayOf(PublicChildModule::class), dependencies = arrayOf(AppComponent::class))
interface PublicChildComponent {
    fun inject(fragment: PublicChildFragment)
}
