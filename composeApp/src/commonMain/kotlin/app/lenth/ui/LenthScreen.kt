package app.lenth.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger

@Composable
fun LenthScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Search", "History")
    var tabPositionText by remember { mutableStateOf(TabPositionText(0.dp, 0.dp)) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                title = { Text("Design your travel", color = Color.White) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.clickable { /* Handle close action */ }
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.clickable { /* Handle settings action */ }
                    )
                }
            )
        },
        backgroundColor = Color.Black
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = Color.Black,
                contentColor = Color.White,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color.White else Color.Gray,
                                modifier = Modifier.onPlaced {
                                    Logger.i("Text offset: ${it.positionOnScreen()}")
                                    tabPositionText = TabPositionText(
                                        width = it.size.width.dp,
                                        left = it.positionInRoot().x.dp
                                    )
                                }
                            )
                        }
                    )
                }
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> SearchTabContent()
                1 -> HistoryTabContent()
            }

            Spacer(modifier = Modifier.weight(1f))

            LenthPrimaryButton(
                text = "Search optimal route",
                textColor = Color.White,
                modifier = Modifier.padding(16.dp),
                backgroundColor = Color(53, 132, 220),
                onClick = { /* Handle search action */ }
            )
        }
    }
}

@Composable
private fun LenthPrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun SearchTabContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        PlaceInputField("e.g. Valencia")
        Spacer(modifier = Modifier.height(16.dp))
        PlaceInputField("e.g. Barcelona")
        Spacer(modifier = Modifier.height(20.dp))
        LenthPrimaryButton(
            text = "Clear all",
            textColor = Color.White,
            backgroundColor = Color.DarkGray,
            onClick = { /* Handle clear all action */ }
        )
    }
}
@Composable
fun PlaceInputField(hint: String) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(8.dp)),
            // .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(hint, color = Color.Gray) },
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.Transparent,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.Gray
            )
        )
    }
}


@Composable
fun HistoryTabContent() {
    // Add the History Tab's content here
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("History Content", color = Color.White)
    }
}

fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPositionText
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    Logger.i(
        tag = "SASD", messageString = "Current tab width: $currentTabWidth and offset: $indicatorOffset"
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset { IntOffset(x = 100, y = 0) }
        .width(currentTabWidth)
}

data class TabPositionText(
    val width: Dp,
    val left: Dp
)
