package net.fpoly.dailymart.repository

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Task

interface TaskRepository {
    suspend fun insertTask(task: Task)

    @Query("select * from task ")
    fun getAllTask(): Flow<List<Task>>

    @Query("select * from task where id_receiver = :id")
    suspend fun getTaskById(id: String): Flow<List<Task>>?

    @Query("select * from task where finish =:finish")
    suspend fun getTaskFinish(finish: Boolean): Flow<List<Task>>?

    @Query("select * from task where createAt >=:startDay")
    suspend fun getTaskByTime(startDay: Long): Flow<List<Task>>?

    @Query("select * from task where id_receiver = :id and finish = :finish")
    suspend fun getTaskByIdAndFinish(id: String, finish: Boolean): Flow<List<Task>>?
}