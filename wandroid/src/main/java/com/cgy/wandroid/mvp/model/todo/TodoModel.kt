package com.cgy.wandroid.mvp.model.todo

import android.app.Application
import com.cgy.wandroid.api.Api
import com.cgy.wandroid.mvp.contract.TodoContract
import com.cgy.wandroid.mvp.model.entity.ApiPagerResponse
import com.cgy.wandroid.mvp.model.entity.ApiResponse
import com.cgy.wandroid.mvp.model.entity.TodoResponse
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import javax.inject.Inject


@ActivityScope
class TodoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), TodoContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getTodoData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getTodoData(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun updateTodoData(id: Int, status: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .doneTodo(id, status))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun deleteTodoData(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .deleteTodo(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
