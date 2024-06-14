package com.qwertyuiop.localData.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.qwertyuiop.localData.entities.PointEntity

@Database(entities = [PointEntity::class], version = 1)
abstract class PointsDatabase : RoomDatabase() {
    abstract val pointsDao: PointsDao
}