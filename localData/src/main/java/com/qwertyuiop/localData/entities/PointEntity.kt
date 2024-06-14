package com.qwertyuiop.localData.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "points_table")
data class PointEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val latitude: Double,
    val longitude: Double,
    val name: String
)
