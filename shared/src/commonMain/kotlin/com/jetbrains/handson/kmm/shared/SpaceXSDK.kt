package com.jetbrains.handson.kmm.shared

import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.network.SpaceXApi
import de.jensklingenberg.ktorfit.converter.builtin.CallResponseConverter
import de.jensklingenberg.ktorfit.converter.builtin.FlowResponseConverter
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import de.jensklingenberg.ktorfit.ktorfit

class SpaceXSDK() {
    private val ktorfit = ktorfit {
        baseUrl("https://api.spacexdata.com/v5/")
        httpClient(HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true
                    useAlternativeNames = false })
            }
        })
        responseConverter(FlowResponseConverter(), CallResponseConverter())
    }

    private val api = ktorfit.create<SpaceXApi>()

    @Throws(Exception::class)
    suspend fun getLaunches(): List<RocketLaunch> {
        return api.getAllLaunches()
    }
}
