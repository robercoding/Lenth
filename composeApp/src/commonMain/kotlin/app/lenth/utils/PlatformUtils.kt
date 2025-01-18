package app.lenth.utils

fun isThereInternetConnection(): Boolean {
    // val konnectivity = Konnectivity()
    // return konnectivity.isConnected // Emit the connectivity changes
    return false
}

expect fun getVersionNameOrNull(): String

expect fun openBrowser(url: String)

expect fun openLanguageSettings()

expect fun copyToClipboard(text: String)

expect fun isFreshInstall(): Boolean