package com.qwertyuiop.asdfghjkl

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.qwertyuiop.appentrypoint.di.AppEntryPointModule.module as AppEntryPointModule
import com.qwertyuiop.localData.di.LocalDataModule.module as LocalDataModule
import com.qwertyuiop.presentation.di.PresentationModule.module as PresentationModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App.applicationContext)
            modules(
                LocalDataModule,
                PresentationModule,
                AppEntryPointModule
            )
        }
    }
}