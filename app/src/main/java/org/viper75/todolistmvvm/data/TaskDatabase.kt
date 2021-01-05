package org.viper75.todolistmvvm.data

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.viper75.todolistmvvm.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Create shutto world app"))
                dao.insert(Task("Create rest api for todos app", important = true))
                dao.insert(Task("Create ui for todos app", completed = true))
                dao.insert(Task("Make ui mock ups"))
            }
        }
    }
}