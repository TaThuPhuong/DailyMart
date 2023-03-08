package net.fpoly.dailymart.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.Task

interface TaskDao {
    /** insert task hoặc thay thế nếu khóa chính task đã tồn tại **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    /** lấy ra toàn bộ task **/
    @Query("select * from task ")
    fun getAllTask(): Flow<List<Task>>

    /** lấy ra task theo id người nhận **/
    @Query("select * from task where id_receiver = :id")
    suspend fun getTaskById(id: String): Flow<List<Task>>?

    /** lấy ra task theo trạng thái hoàn thành **/
    @Query("select * from task where finish =:finish")
    suspend fun getTaskFinish(finish: Boolean): Flow<List<Task>>?

    /** lấy ra task trong ngày điều kiện : truyền vào thời gian bắt đầu của ngày đó **/
    @Query("select * from task where createAt >=:startDay")
    suspend fun getTaskByTime(startDay: Long): Flow<List<Task>>?

    /** lấy ra task theo id người nhânj và trạng thái hoàn thành **/
    @Query("select * from task where id_receiver = :id and finish = :finish")
    suspend fun getTaskByIdAndFinish(id: String, finish: Boolean): Flow<List<Task>>?
}