package com.jetbrains.handson.kmm.shared

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.network.SpaceXApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SpaceXSDK() {
    private val api = SpaceXApi()

    @Throws(Exception::class)
    fun getLaunches(): Flow<List<RocketLaunch>> {
        return api.getAllLaunches()
    }
}
