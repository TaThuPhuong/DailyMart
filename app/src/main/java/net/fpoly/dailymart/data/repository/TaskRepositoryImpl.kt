package net.fpoly.dailymart.data.repository

import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.database.TaskDao
import net.fpoly.dailymart.data.model.Task
import net.fpoly.dailymart.repository.TaskRepository

class TaskRepositoryImpl(private val dao: TaskDao) : TaskRepository {
    override suspend fun insertTask(task: Task) {
        dao.insertTask(task)
    }

    override fun getAllTask(): Flow<List<Task>> {
        return dao.getAllTask()
    }

    override suspend fun getTaskById(id: String): Flow<List<Task>>? {
        return dao.getTaskById(id)
    }

    override suspend fun getTaskFinish(finish: Boolean): Flow<List<Task>>? {
        return dao.getTaskFinish(finish)
    }

    override suspend fun getTaskByTime(startDay: Long): Flow<List<Task>>? {
        return dao.getTaskByTime(startDay)
    }

    override suspend fun getTaskByIdAndFinish(id: String, finish: Boolean): Flow<List<Task>>? {
        return dao.getTaskByIdAndFinish(id, finish)
    }

}