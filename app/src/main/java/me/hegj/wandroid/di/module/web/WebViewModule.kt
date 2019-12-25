package me.hegj.wandroid.di.module.web

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.web.WebViewContract
import me.hegj.wandroid.mvp.model.web.WebViewModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/10/2019 09:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建WebviewModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class WebViewModule(private val view: WebViewContract.View) {
    @ActivityScope
    @Provides
    fun provideWebviewView(): WebViewContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideWebviewModel(model: WebViewModel): WebViewContract.Model {
        return model
    }
}
