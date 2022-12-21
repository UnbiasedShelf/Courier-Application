package by.bstu.vs.stpms.courier_application.model.database.dao

import androidx.room.Dao
import androidx.room.Query
import by.bstu.vs.stpms.courier_application.model.database.entity.Destination

@Dao
abstract class DestinationDao : AbstractDao<Destination>() {
    @Query("DELETE FROM destination WHERE 1=1")
    abstract override suspend fun truncate()
    @Query("SELECT * FROM destination WHERE id = :id")
    abstract override suspend fun findById(id: Long): Destination?

    @Query("SELECT * FROM destination")
    abstract override suspend fun getAll(): List<Destination>
}