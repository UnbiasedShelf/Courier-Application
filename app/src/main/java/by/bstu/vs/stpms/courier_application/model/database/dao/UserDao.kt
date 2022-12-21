package by.bstu.vs.stpms.courier_application.model.database.dao

import androidx.room.*
import by.bstu.vs.stpms.courier_application.model.database.entity.Role
import by.bstu.vs.stpms.courier_application.model.database.entity.User

@Dao
abstract class UserDao {
    @Insert
    abstract fun insert(user: User)
    @Delete
    abstract fun delete(user: User)
    @Update
    abstract fun update(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWithReplace(item: User)
//    @Query("SELECT * FROM user LIMIT 1")
//    abstract suspend fun getUser(): List<User>?

    @Query("SELECT * FROM role WHERE id IN (select roleId from user_role where userId = :userId)")
    abstract suspend fun getRolesByUserId(userId: Long): List<Role>

    @Query("DELETE FROM user WHERE 1=1")
    abstract suspend fun truncate()

    @Query("SELECT * FROM user WHERE id = :id")
    abstract suspend fun findById(id: Long): User?

    @Query("SELECT * FROM user")
    abstract suspend fun getAll(): List<User>
}