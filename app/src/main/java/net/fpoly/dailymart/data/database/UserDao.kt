package net.fpoly.dailymart.data.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.fpoly.dailymart.data.model.User
import net.fpoly.dailymart.utils.ROLE

interface UserDao {
    /** thêm mới hoặc thay thế 1 user nếu id user đã tồn tại **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    /** lấy ra toàn bộ user **/
    @Query("select * from user")
    fun getAllUser(): Flow<List<User>>?

    /** lấy ra user theo id **/
    @Query("select * from user where id =:id")
    suspend fun getUserById(id: String): User?

    /** lấy ra user theo vai trò  admin, manage, staff**/
    @Query("select * from user where role = :role")
    suspend fun getUserByRole(role: ROLE): Flow<List<User>>?

    /** lấy ra user theo trạng thái vô hiệu hóa **/
    @Query("select * from user where disable =:disable")
    suspend fun getUserDisable(disable: Boolean): Flow<List<User>>?
}