package app.lenth.utils

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APP_LOCALE_SETTINGS
import android.util.Log
import co.touchlab.kermit.Logger

actual fun getVersionNameOrNull(): String = "asd"

actual fun openBrowser(url: String) {
    try {
        val context = applicationContext
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }catch (e: Exception){
        Logger.e { "openBrowser: $e" }
    }
}

actual fun copyToClipboard(text: String) {
    val context = applicationContext
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

actual fun openLanguageSettings() {
    Log.d("PlatformUtils", "openLanguageSettings")
    val context = activityContext
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val intent = Intent(ACTION_APP_LOCALE_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    } else {
        val intent = Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS)
        context.startActivity(intent)
    }
}

actual fun isFreshInstall(): Boolean {
    return with(applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)) {
        // val daysDifference = getCurrentLocalDateTimeTikbitFormatKt().dayOfYear - firstInstallTime.toLocalDateTimeTikbitFormat().dayOfYear
        // daysDifference < 7
        true
    }
}