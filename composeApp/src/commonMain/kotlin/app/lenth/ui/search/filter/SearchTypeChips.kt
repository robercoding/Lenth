package app.lenth.ui.search.filter

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

// Define the search types as an enum
enum class SearchTypeUi(val displayName: String) {
    ALL("All"),
    ADDRESSES("Addresses"),
    CITIES_TOWNS("Cities/Towns"),
    REGIONS("Regions"),
    LANDMARKS("Landmarks"),
    BUSINESSES_PLACES("Businesses/Places"),
    POSTAL_CODES("Postal Codes"),
    TRANSIT_STATIONS("Transit Stations"),
    NATURAL_FEATURES("Natural Features"),
    NEIGHBORHOODS("Neighborhoods");
}

// Composable function to display the row of chips
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchTypeChips(
    onChipSelected: (SearchTypeUi) -> Unit
) {
    var selectedChip by remember { mutableStateOf(SearchTypeUi.ALL) }

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SearchTypeUi.values().forEach { searchType ->
            Chip(
                searchType = searchType,
                isSelected = searchType == selectedChip,
                onClick = {
                    selectedChip = searchType
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
        SearchTypeChips { selectedType ->
            println("Selected: ${selectedType.name}")
        }
    }
}