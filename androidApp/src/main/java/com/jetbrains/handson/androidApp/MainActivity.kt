package com.jetbrains.handson.androidApp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jetbrains.handson.androidApp.ui.theme.AppTheme
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch
import com.jetbrains.handson.kmm.shared.viewmodel.MoreContent

class MainActivity : ComponentActivity() {

    private val viewModel: AndroidMainViewModel by viewModels()

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
                        RocketLaunches(viewModel, paddingValues, ::showMore)
                    }
                }
            }
        }
    }

    private fun showMore(rocketLaunch: RocketLaunch) {
        when (val showMoreContent = viewModel.getMoreContent(rocketLaunch)) {
            is MoreContent.ArticleContent -> showWebsite(showMoreContent.link)
            MoreContent.NoContent -> showAlertForNoMoreContent()
            is MoreContent.WikipediaContent -> showWebsite(showMoreContent.link)
            is MoreContent.YoutubeContent -> maybeShowYoutube(
                showMoreContent.link,
                showMoreContent.id
            )
        }
    }

    private fun showWebsite(link: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        startActivity(intent)
    }

    private fun maybeShowYoutube(link: String, id: String) {
        val appIntent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "vnd.youtube:$id"
            )
        )
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(link)
        )
        try {
            startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            startActivity(webIntent)
        }
    }

    private fun showAlertForNoMoreContent() {
        Toast.makeText(this, "No more content", Toast.LENGTH_LONG).show()
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
fun RocketLaunch(modifier: Modifier, rocketLaunch: RocketLaunch, showMore: (RocketLaunch) -> Unit) {
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
            Button(onClick = { showMore(rocketLaunch) }) {
                Text(text = stringResource(id = R.string.show_more_field))
            }
        }
    }
}

@Composable
fun TopBar() {
    Text(text = stringResource(id = R.string.app_name))
}

@Composable
fun RocketLaunches(
    viewModel: AndroidMainViewModel,
    paddingValues: PaddingValues,
    onRocketLaunch: (RocketLaunch) -> Unit
) {
    val launches = remember { mutableStateOf(viewModel.launches) }.value.collectAsState()
    if (launches.value.isEmpty()) {
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
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = launches.value) { it ->
                    RocketLaunch(
                        modifier = Modifier.padding(8.dp),
                        rocketLaunch = it,
                        showMore = { rocketLaunch -> onRocketLaunch(rocketLaunch) }
                    )
                }
            }
        }
    }
}