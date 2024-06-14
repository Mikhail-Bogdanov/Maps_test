package com.qwertyuiop.localData.di

import androidx.room.Room
import com.qwertyuiop.localData.database.PointsDatabase
import com.qwertyuiop.localData.repository.PointsRepository
import com.qwertyuiop.localData.repository.PointsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object LocalDataModule {

    val module = module {
        single {
            Room.databaseBuilder(
                androidContext(),
                PointsDatabase::class.java,
                APP_DATABASE_NAME
            ).build().pointsDao
        }

        singleOf(::PointsRepositoryImpl) bind PointsRepository::class
    }

    private const val APP_DATABASE_NAME = "points_database"
}