package com.cgy.wandroid.di.component

import com.cgy.wandroid.di.module.LoginModule
import com.cgy.wandroid.ui.login.LoginActivity
import com.cgy.wandroid.ui.login.RegisterActivity
import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/16 17:52
 */
@ActivityScope
@Component(modules = arrayOf(LoginModule::class), dependencies = arrayOf(AppComponent::class))
interface LoginComponent {

    fun inject(activity: LoginActivity)

    fun injectRegister(activity: RegisterActivity)
}