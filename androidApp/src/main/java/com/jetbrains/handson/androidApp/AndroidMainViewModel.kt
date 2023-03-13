package com.jetbrains.handson.androidApp

import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.viewmodel.MainViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AndroidMainViewModel() : MainViewModel() {

    private val sdk: SpaceXSDK = SpaceXSDK()

    val launches: StateFlow<List<RocketLaunch>> = sdk.getLaunches().stateIn(
        scope = viewModelScope.coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )
}