package com.cgy.wandroid.di.module

import com.cgy.wandroid.mvp.contract.LoginContract
import com.cgy.wandroid.mvp.model.login.LoginModel
import com.jess.arms.di.scope.ActivityScope
import dagger.Module
import dagger.Provides

/**
 * @author: cgy
 * @description:
 * @date: 2019/9/16 17:44
 */
@Module
//构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class LoginModule(private val view : LoginContract.View) {

    @ActivityScope
    @Provides
    fun provideLoginView() : LoginContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideLoginModel(model: LoginModel) : LoginContract.Model {
        return model
    }
}