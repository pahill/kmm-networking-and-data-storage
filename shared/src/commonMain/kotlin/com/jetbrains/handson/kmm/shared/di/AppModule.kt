package com.jetbrains.handson.kmm.shared.di

import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.network.SpaceXApi
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

object Modules {
    val appModule = module {
        factory { SpaceXApi() }
        factory { SpaceXSDK(get()) }
    }
}

fun initKoin(
    appModule: Module = Modules.appModule
): KoinApplication = startKoin {
    modules(appModule)
}
