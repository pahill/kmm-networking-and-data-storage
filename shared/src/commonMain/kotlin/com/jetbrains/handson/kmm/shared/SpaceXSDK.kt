package com.jetbrains.handson.kmm.shared

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.network.SpaceXApi

class SpaceXSDK(val api: SpaceXApi) {

    @Throws(Exception::class)
    suspend fun getLaunches(): List<RocketLaunch> {
        return api.getAllLaunches()
    }
}
