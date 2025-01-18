package app.lenth.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileCreationDate
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIDevice
import platform.UIKit.UIPasteboard

actual fun getVersionNameOrNull(): String {
    return UIDevice.currentDevice.systemVersion
}


actual fun openBrowser(url: String) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl)
    }


    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl, emptyMap<Any?, Any?>()) { success ->
            if (!success) {
                println("Failed to open URL: $url")
            }
        }
    }
}

actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
}

actual fun openLanguageSettings() {
    val url = NSURL(string = "$UIApplicationOpenSettingsURLString/app.lenth")

    if (UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url, emptyMap<Any?, Any?>()) { success ->
            if (!success) {
                println("Failed to open URL: $url")
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun isFreshInstall(): Boolean {
    val fileManager = NSFileManager.defaultManager
    val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
    val documentsDirectory = urls.last() as NSURL

    val attributes = fileManager.attributesOfItemAtPath(documentsDirectory.path!!, null)
    val installationDate = attributes?.get(NSFileCreationDate) as NSDate

    val calendar = NSCalendar.currentCalendar
    val now = NSDate()

    val components = calendar.components(NSCalendarUnitDay, fromDate = installationDate, toDate = now, options = 0u)
    val daysDifference = components.day

    return daysDifference < 7
}