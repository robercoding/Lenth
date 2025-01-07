package app.lenth.ui.search.filter

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

// Define the search types as an enum


// Composable function to display the row of chips
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchTypeChips(
    selectedSearchType: SearchTypeUi,
    onChipSelected: (SearchTypeUi) -> Unit
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchTypeUi.values().forEach { searchType ->
            Chip(
                searchType = searchType,
                isSelected = searchType == selectedSearchType,
                onClick = {
                    onChipSelected(searchType)
                }
            )
        }
    }
}

// Composable for an individual chip
@Composable
fun Chip(
    searchType: SearchTypeUi,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) Color(53, 132, 220) else Color.DarkGray
    )

    Surface(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        // color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        // color = if (isSelected) Color(53, 132, 220) else Color.DarkGray,
        color = color,
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        shadowElevation = 1.dp
    ) {
        Text(
            text = searchType.displayName,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            // color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            color = Color.White
        )
    }
}

// Preview
@Composable
@Preview
fun SearchTypeChipsPreview() {
    MaterialTheme {
        SearchTypeChips(
            selectedSearchType = SearchTypeUi.ALL
        ) { selectedType ->
            println("Selected: ${selectedType.name}")
        }
    }
}