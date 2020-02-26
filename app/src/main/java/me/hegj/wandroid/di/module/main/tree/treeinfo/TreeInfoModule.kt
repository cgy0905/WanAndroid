package me.hegj.wandroid.di.module.main.tree.treeinfo

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.tree.treeinfo.TreeInfoContract
import me.hegj.wandroid.mvp.model.main.tree.treeinfo.TreeInfoModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/23/2019 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建TreeInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TreeInfoModule(private val view: TreeInfoContract.View) {
    @FragmentScope
    @Provides
    fun provideTreeInfoView(): TreeInfoContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideTreeInfoModel(model: TreeInfoModel): TreeInfoContract.Model {
        return model
    }
}
