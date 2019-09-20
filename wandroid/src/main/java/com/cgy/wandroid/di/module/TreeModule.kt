package com.cgy.wandroid.di.module

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import com.cgy.wandroid.mvp.contract.TreeContract
import com.cgy.wandroid.mvp.model.main.tree.TreeModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/20/2019 10:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建TreeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TreeModule(private val view: TreeContract.View) {
    @FragmentScope
    @Provides
    fun provideTreeView(): TreeContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideTreeModel(model: TreeModel): TreeContract.Model {
        return model
    }
}
