package org.viper75.todolistmvvm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getAllTasks(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_NAME -> getAllTaskSortedByName(searchQuery, hideCompleted)
            SortOrder.BY_DATE -> getAllTaskSortedByDateCreated(searchQuery, hideCompleted)
        }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed == 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getAllTaskSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed == 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getAllTaskSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}