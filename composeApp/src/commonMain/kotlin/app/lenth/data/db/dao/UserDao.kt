package app.lenth.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import app.lenth.data.db.models.OptimalRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {

    @Insert
    suspend fun insert(optimalRouteEntity: OptimalRouteEntity): Long

    @Query("SELECT * FROM optimal_route WHERE id = :id")
    suspend fun getRouteById(id: Int): OptimalRouteEntity?

    @Delete
    suspend fun delete(optimalRouteEntity: OptimalRouteEntity)

    @Query("DELETE FROM optimal_route WHERE id = :id")
    suspend fun deleteById(id: Int): Int

    @Query("DELETE FROM optimal_route")
    suspend fun clearAllRoutes()

    @Query("SELECT * FROM optimal_route")
    suspend fun getAllRoutes(): List<OptimalRouteEntity>

    @Query("SELECT * FROM optimal_route")
    fun getAllRoutesFlow(): Flow<List<OptimalRouteEntity>>
}