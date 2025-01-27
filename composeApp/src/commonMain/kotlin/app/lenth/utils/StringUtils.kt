package app.lenth.utils

import kotlin.math.pow
import kotlin.math.round

fun Double.formatToDistanceKm(): String {
    return "${this.toTwoDecimalString()} km"
}

// Create af unction that formats the distance withotu decimals
fun Double.formatToDistanceKmNoDecimals(): Int = toInt()

private fun Double.toTwoDecimalString(): String {
    val factor = 10.0.pow(2) // Multiply by 100 for 2 decimals
    val roundedValue = round(this * factor) / factor
    return roundedValue.toString()
}