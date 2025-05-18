package com.example.jetpack_compose_assignment_2.di

import android.content.Context
import androidx.room.Room
import com.example.jetpack_compose_assignment_2.data.api.ApiService
import com.example.jetpack_compose_assignment_2.data.local.dao.TodoDao
import com.example.jetpack_compose_assignment_2.data.local.database.ToDoDatabase
import com.example.jetpack_compose_assignment_2.data.repositories.ToDoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ToDoDatabase =
        Room.databaseBuilder(appContext, ToDoDatabase::class.java, "todo_db").build()

    @Provides
    fun provideTodoDao(db: ToDoDatabase): TodoDao = db.todoDao

    @Provides
    @Singleton
    fun provideTodoRepository(apiService: ApiService, todoDao: TodoDao): ToDoRepository {
        return ToDoRepository(apiService, todoDao)
    }
}