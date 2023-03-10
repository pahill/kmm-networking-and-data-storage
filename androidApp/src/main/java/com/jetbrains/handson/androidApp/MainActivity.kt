package com.jetbrains.handson.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jetbrains.handson.androidApp.ui.theme.AppTheme
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val sdk: SpaceXSDK by inject()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { TopBar() })
                        }
                    ) { paddingValues ->
                        val refreshScope = rememberCoroutineScope()
                        var refreshing by remember { mutableStateOf(false) }
                        var listRocketLaunch by remember { mutableStateOf(emptyList<RocketLaunch>()) }

                        LaunchedEffect(Unit) {
                            listRocketLaunch = sdk.getLaunches()
                        }

                        fun refresh() {
                            refreshScope.launch {
                                refreshing = true
                                listRocketLaunch = sdk.getLaunches()
                                refreshing = false
                            }
                        }

                        PullToRefreshRocketLaunches(
                            refreshing = refreshing,
                            refresh = ::refresh,
                            listRocketLaunch = listRocketLaunch,
                            modifier = Modifier.padding((paddingValues))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MissionStatus(modifier: Modifier, launchSuccess: Boolean?) {
    val (text: String, color: Color) = when (launchSuccess) {
        true -> stringResource(R.string.successful) to colorResource(R.color.colorSuccessful)
        false -> stringResource(R.string.unsuccessful) to colorResource(R.color.colorUnsuccessful)
        null -> stringResource(R.string.no_data) to colorResource(R.color.colorNoData)
    }
    Text(modifier = modifier, text = text, color = color)
}

@Composable
fun RocketLaunch(modifier: Modifier, rocketLaunch: RocketLaunch) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = stringResource(id = R.string.mission_name_field).format(rocketLaunch.missionName))
            MissionStatus(Modifier.padding(top = 8.dp), rocketLaunch.launchSuccess)
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.launch_year_field).format(rocketLaunch.launchYear)
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.details_field).format(
                    rocketLaunch.details ?: ""
                )
            )
        }
    }
}

@Composable
fun TopBar() {
    Text(text = stringResource(id = R.string.app_name))
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToRefreshRocketLaunches(
    modifier: Modifier,
    refreshing: Boolean,
    listRocketLaunch: List<RocketLaunch>,
    refresh: () -> Unit,
) {
    val state = rememberPullRefreshState(refreshing, refresh)
    Box(
        modifier = modifier.pullRefresh(state)
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            if (!refreshing) {
                items(listRocketLaunch) {
                    RocketLaunch(Modifier.padding(8.dp), rocketLaunch = it)
                }
            }
        }

        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
}