package com.cgy.wandroid.di.module


import com.cgy.wandroid.mvp.contract.MainContract
import com.cgy.wandroid.mvp.model.MainModel
import com.jess.arms.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

/**
 * ================================================
 * Description:
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MainModule(private val view: MainContract.View) {
    @FragmentScope
    @Provides
    fun provideMainView(): MainContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideMainModel(model : MainModel) : MainContract.Model {
        return model
    }


}
