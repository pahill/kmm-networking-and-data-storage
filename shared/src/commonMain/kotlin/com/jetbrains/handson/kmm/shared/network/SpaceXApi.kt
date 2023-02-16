package com.jetbrains.handson.kmm.shared.network

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    suspend fun getAllLaunches(): MutableStateFlow<List<RocketLaunch>> {
        println("hi")
//        val rocketLaunches: List<RocketLaunch> =
//            httpClient.get("https://api.spacexdata.com/v5/launches").body()
//        println("rocketLaunched received")
        val progressiveList = mutableListOf<RocketLaunch>()
        val mutableStateFlow = MutableStateFlow(listOf<RocketLaunch>())
        coroutineScope.launch {
            httpClient.get("https://api.spacexdata.com/v5/launches").body<List<RocketLaunch>>().forEach { rocketLaunch ->
                println(rocketLaunch)
                delay(1000)
                progressiveList.add(0, rocketLaunch)
               // println(progressiveList.count())
                mutableStateFlow.value = progressiveList.toList()
            }
        }
        println("finished launching")

        return mutableStateFlow
    }
}

