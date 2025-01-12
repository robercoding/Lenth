package app.lenth

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale
import okhttp3.internal.platform.AndroidPlatform

class AndroidPlatform(context: Context) : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val version: String = context.getVersionName()
    override val language: String
        get() = AppCompatDelegate.getApplicationLocales()[0]?.language ?: Locale.getDefault().language

    override val type: Platform.Type = Platform.Type.ANDROID
}

fun Context.getVersionName(): String = packageManager.getPackageInfo(packageName, 0)?.versionName ?: error("Version name not found")