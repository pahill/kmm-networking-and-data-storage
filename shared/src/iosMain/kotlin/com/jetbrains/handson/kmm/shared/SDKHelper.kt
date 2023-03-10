package com.jetbrains.handson.kmm.shared

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SDKHelper : KoinComponent{
    private val sdk: SpaceXSDK by inject()

    suspend fun getLaunches() = sdk.getLaunches()
}