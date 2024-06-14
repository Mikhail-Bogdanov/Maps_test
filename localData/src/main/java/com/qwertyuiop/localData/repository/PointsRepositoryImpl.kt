package com.qwertyuiop.localData.repository

import com.qwertyuiop.localData.database.PointsDao
import com.qwertyuiop.localData.entities.PointEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PointsRepositoryImpl(
    private val pointsDao: PointsDao
) : PointsRepository {

    override fun getAllPoints() = pointsDao.selectAllPoints()

    override suspend fun addPoint(pointEntity: PointEntity) = withContext(Dispatchers.IO) {
        pointsDao.insertPoint(pointEntity)
    }

    override suspend fun removePointById(pointId: String) = withContext(Dispatchers.IO) {
        pointsDao.deletePointById(pointId)
    }
}