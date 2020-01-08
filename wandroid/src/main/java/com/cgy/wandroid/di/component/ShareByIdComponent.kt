package com.cgy.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import com.cgy.wandroid.di.module.ShareByIdModule

import com.jess.arms.di.scope.ActivityScope
import com.cgy.wandroid.ui.share.ShareByIdActivity


@ActivityScope
@Component(modules = arrayOf(ShareByIdModule::class), dependencies = arrayOf(AppComponent::class))
interface ShareByIdComponent {
    fun inject(activity: ShareByIdActivity)
}
