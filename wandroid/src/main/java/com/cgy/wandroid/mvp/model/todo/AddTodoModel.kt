package com.cgy.wandroid.mvp.model.todo

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.ActivityScope
import javax.inject.Inject

import com.cgy.wandroid.mvp.contract.AddTodoContract


@ActivityScope
class AddTodoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), AddTodoContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun onDestroy() {
        super.onDestroy()
    }
}
