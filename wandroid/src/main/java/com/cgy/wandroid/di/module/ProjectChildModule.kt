package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.ProjectChildContract
import com.cgy.wandroid.mvp.model.main.project.ProjectChildModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/30/2019 11:22
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建ProjectChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ProjectChildModule(private val view: ProjectChildContract.View) {
    @FragmentScope
    @Provides
    fun provideProjectChildView(): ProjectChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideProjectChildModel(model: ProjectChildModel): ProjectChildContract.Model {
        return model
    }
}
