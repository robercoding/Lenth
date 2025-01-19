package app.lenth.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.utils.formatToDistanceKm
import app.lenth.utils.formatToDistanceKmNoDecimals
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryTabContent(
    historyViewModel: HistoryViewModel = koinViewModel(),
) {
    // Add the History Tab's content here
    val state = historyViewModel.state.collectAsStateWithLifecycle(null).value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
            .navigationBarsPadding(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            contentAlignment = androidx.compose.ui.Alignment.CenterStart,
        ) {
            Text(
                text = "Recent optimal routes",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier,
            )
        }
        state?.let {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(it, key = { item -> item.id }) {

                    Column {

                    CustomListItem(
                        modifier = Modifier.fillMaxWidth(),
                        imageUrl = it.imageStart,
                        title = "From ${it.path.first()} to ${it.path.last()}",
                        locations = it.path.size,
                        distance = it.distance,
                        onClick = {}
                    )

                    // ListItem(
                    //     // tonalElevation = 2.dp,
                    //     modifier = Modifier
                    //         .clip(RoundedCornerShape(12.dp))
                    //         .clickable {
                    //
                    //         },
                    //     leadingContent = {
                    //         Box(modifier = Modifier.fillMaxHeight().background(Color.Red), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    //             AsyncImage(
                    //                 model = it.imageStart,
                    //                 // model = "https://maps.googleapis.com/maps/api/staticmap?center=Massamagrell,Spain&zoom=12&size=600x400&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY",
                    //                 // model = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/1745px-Android_robot.svg.png",
                    //                 // contentDescription = null,
                    //                 contentDescription = "image",
                    //                 modifier = Modifier
                    //                     .size(70.dp)
                    //                     .clip(RoundedCornerShape(8.dp)),
                    //                 contentScale = ContentScale.Crop,
                    //                 onError = {},
                    //                 onSuccess = {
                    //                 },
                    //                 onLoading = {
                    //                 },
                    //                 // loading = {
                    //                 //     Text("Loading...", color = Color.Black)
                    //                 //     // CircularProgressIndicator()
                    //                 // }
                    //             )
                    //         }
                    //     },
                    //     headlineContent = {
                    //         Text("From ${it.path.first()} to ${it.path.last()}", color = MaterialTheme.colorScheme.onSurface)
                    //     },
                    //     supportingContent = {
                    //         // Number of cities and total distance
                    //         Column {
                    //             Text("Cities: ${it.path.size}", color = MaterialTheme.colorScheme.onSurface)
                    //             Text("Distance: ${it.distance.formatToDistanceKmNoDecimals()}", color = MaterialTheme.colorScheme.onSurface)
                    //         }
                    //         // Text("Cities: ${it.path.size} - Distance: ${it.distance.formatToDistanceKm()}", color = MaterialTheme.colorScheme.onSurface)
                    //     },
                    //     trailingContent = {
                    //         Icon(
                    //             imageVector = Icons.Default.ChevronRight,
                    //             contentDescription = Icons.Default.ChevronRight.name,
                    //             tint = MaterialTheme.colorScheme.onSurface,
                    //         )
                    //     },
                    // )
                    }
                }
            }
        }
    }
}