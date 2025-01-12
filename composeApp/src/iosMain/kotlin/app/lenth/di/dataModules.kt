package app.lenth.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.lenth.data.preferences.PREFERENCE_DATA_STORE_SETTINGS
import app.lenth.data.preferences.createSharedDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual val dataModule: Module = module {
    single { createDataStore() }
}

@OptIn(ExperimentalForeignApi::class)
private fun createDataStore(): DataStore<Preferences> = createSharedDataStore {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    requireNotNull(documentDirectory).path + "/$PREFERENCE_DATA_STORE_SETTINGS"
}