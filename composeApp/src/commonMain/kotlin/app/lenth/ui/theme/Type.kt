package app.lenth.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.roboto_black
import lenth.composeapp.generated.resources.roboto_bold
import lenth.composeapp.generated.resources.roboto_light
import lenth.composeapp.generated.resources.roboto_medium
import lenth.composeapp.generated.resources.roboto_regular
import lenth.composeapp.generated.resources.roboto_thin
import org.jetbrains.compose.resources.Font

val AppTypography = Typography()

@Composable
fun RobotoFontFamily() = FontFamily(
    Font(Res.font.roboto_thin, FontWeight.Thin),
    Font(Res.font.roboto_light, FontWeight.Light),
    Font(Res.font.roboto_regular, FontWeight.Normal),
    Font(Res.font.roboto_medium, FontWeight.Medium),
    Font(Res.font.roboto_bold, FontWeight.Bold),
    Font(Res.font.roboto_black, FontWeight.Black),
)


@Composable
private fun LenthTypography(): Typography {
    val appFont = RobotoFontFamily()
    return Typography(
        displayLarge = Typography().displayLarge.copy(fontFamily = appFont), // 57.sp
        displayMedium = Typography().displayMedium.copy(fontFamily = appFont), // 45.sp
        displaySmall = Typography().displaySmall.copy(fontFamily = appFont), // 36.sp
        headlineLarge = Typography().headlineLarge.copy(fontFamily = appFont), // 32.sp
        headlineMedium = Typography().headlineMedium.copy(fontFamily = appFont), // 26.sp
        headlineSmall = Typography().headlineSmall.copy(fontFamily = appFont), // 24.sp
        titleLarge = Typography().titleLarge.copy(fontFamily = appFont), // 22.sp
        bodyLarge = Typography().bodyLarge.copy(fontFamily = appFont), // 16.sp (Body)
        bodyMedium = Typography().bodyMedium.copy(fontFamily = appFont), // 14.sp (Headers)
        titleMedium = Typography().titleMedium.copy(fontFamily = appFont), // 16.sp
        titleSmall = Typography().titleSmall.copy(fontFamily = appFont), // 14 sp, e.g: Supermarket
        labelLarge = Typography().labelLarge.copy(fontFamily = appFont), // 14.sp
        bodySmall = Typography().bodySmall.copy(fontFamily = appFont), // 12.sp (Ticket details, product name, price etc..)
        labelMedium = Typography().labelMedium.copy(fontFamily = appFont), // 12.sp
        labelSmall = Typography().labelSmall.copy(fontFamily = appFont), // 11sp Small things, e.g: dates
    )
}