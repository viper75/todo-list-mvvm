package org.viper75.todolistmvvm.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.viper75.todolistmvvm.data.TaskDatabase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(application: Application, callback: TaskDatabase.Callback) = Room
        .databaseBuilder(application, TaskDatabase::class.java, "task_database")
        .addCallback(callback)
        .build()

    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(value = AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope