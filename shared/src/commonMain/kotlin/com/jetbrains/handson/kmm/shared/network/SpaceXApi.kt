package com.jetbrains.handson.kmm.shared.network

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class SpaceXApi {
    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()


    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    fun getAllLaunches(): Flow<List<RocketLaunch>> = flow {
        var progressiveList = listOf<RocketLaunch>()
        httpClient.get("https://api.spacexdata.com/v5/launches").body<List<RocketLaunch>>()
            .forEach { rocketLaunch ->
                progressiveList = listOf(rocketLaunch) + progressiveList
                println("progressiveList ${progressiveList.size}")
                emit(progressiveList)
                delay(1000)
            }
    }
}

