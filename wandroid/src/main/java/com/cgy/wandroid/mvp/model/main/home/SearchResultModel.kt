package com.cgy.wandroid.mvp.model.main.home

import android.app.Application
import com.cgy.wandroid.mvp.contract.SearchResultContract
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import javax.inject.Inject


@ActivityScope
class SearchResultModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), SearchResultContract.Model{
    @Inject
    lateinit var mGson:Gson
    @Inject
    lateinit var mApplication:Application



    override fun onDestroy() {
          super.onDestroy()
    }
}
