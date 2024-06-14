package com.qwertyuiop.localData.repository

import com.qwertyuiop.localData.entities.PointEntity
import kotlinx.coroutines.flow.Flow

interface PointsRepository {
    fun getAllPoints(): Flow<List<PointEntity>>

    suspend fun addPoint(pointEntity: PointEntity)

    suspend fun removePointById(pointId: String)
}