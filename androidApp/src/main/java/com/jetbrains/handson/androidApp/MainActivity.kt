package com.jetbrains.handson.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.jetbrains.handson.androidApp.ui.theme.AppTheme
import com.jetbrains.handson.kmm.shared.SpaceXSDK
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class, ExperimentalLifecycleComposeApi::class)
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
                        RocketLaunches(viewModel, paddingValues)
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
            Text(
                text = stringResource(id = R.string.mission_name_field).format(
                    rocketLaunch.missionName
                )
            )
            MissionStatus(
                Modifier.padding(top = 8.dp),
                rocketLaunch.launchSuccess
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.launch_year_field).format(
                    rocketLaunch.launchYear
                )
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

@Composable
fun RocketLaunches(
    viewModel: MainViewModel,
    paddingValues: PaddingValues
) {
    val launches = viewModel.launches
    val launchesState = launches.collectAsState()
    if (launchesState.value.isEmpty()) {
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                text = "Loading...",
                modifier = Modifier
                    .align(Center)
            )
        }
    } else {
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            println("composable ${launchesState.value.size}")
            Column(modifier = Modifier.fillMaxWidth()) {
                launchesState.value.forEach {
                    RocketLaunch(
                        modifier = Modifier.padding(8.dp),
                        rocketLaunch = it
                    )
                }
            }
        }
    }
}

