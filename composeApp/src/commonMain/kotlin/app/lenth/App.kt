package app.lenth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.lenth.data.GeoCodingRepository
import app.lenth.di.defaultModules
import app.lenth.di.networkModule
import app.lenth.di.repositoryModule
import app.lenth.di.useCaseModules
import app.lenth.di.viewModelModules
import app.lenth.ui.LenthScreen
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {

    KoinApplication(
        application = { modules(defaultModules, networkModule, repositoryModule, viewModelModules, useCaseModules) }
    ) {
        val greeting = koinInject<Greeting>()
        val repository = koinInject<GeoCodingRepository>()
        val coroutineScope = rememberCoroutineScope()
        MaterialTheme {
            var showContent by remember { mutableStateOf(false) }
            LenthScreen()
            // Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            //     Button(onClick = { showContent = !showContent }) {
            //         Text("Click me!")
            //         coroutineScope.launch {
            //             val response = repository.getGeoCoding()
            //             Logger.i(tag = "this.", throwable = null, messageString = "Response: $response")
            //         }
            //     }
            //     AnimatedVisibility(showContent) {
            //         Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            //             Image(painterResource(Res.drawable.compose_multiplatform), null)
            //             Text("Compose: ${greeting.greet()}")
            //         }
            //     }
            // }
        }
    }
}