package com.qwertyuiop.localData.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.qwertyuiop.localData.entities.PointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {

    @Query("SELECT * FROM points_table")
    fun selectAllPoints(): Flow<List<PointEntity>>

    @Insert
    suspend fun insertPoint(pointEntity: PointEntity)

    @Query("DELETE FROM points_table WHERE id = :pointId")
    suspend fun deletePointById(pointId: String)

}