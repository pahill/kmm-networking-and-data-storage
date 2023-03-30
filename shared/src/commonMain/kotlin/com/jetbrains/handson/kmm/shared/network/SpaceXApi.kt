package com.jetbrains.handson.kmm.shared.network

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import de.jensklingenberg.ktorfit.http.GET
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

interface SpaceXApi {
    @GET("launches")
    suspend fun getAllLaunches(): List<RocketLaunch>
}

