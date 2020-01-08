package com.cgy.wandroid.mvp.model.todo

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.AddTodoContract
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@ActivityScope
class AddTodoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), AddTodoContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    override fun addTodo(title: String, content: String, date: String, type: Int, priority: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .addTodo(title, content, date, type, priority))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable

                }
    }

    override fun updateTodo(title: String, content: String, date: String, type: Int, priority: Int, id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .updateTodo(title, content, date, type, priority, id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
