package app.lenth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import app.lenth.data.preferences.DevelopmentPreference
import app.lenth.data.preferences.PREFERENCE_DATA_STORE_SETTINGS
import app.lenth.data.preferences.createSharedDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataModule: Module = module {
    single { createDataStore(androidContext(), PREFERENCE_DATA_STORE_SETTINGS) }
}

fun createDataStore(context: Context, fileName: String): DataStore<Preferences> = createSharedDataStore { context.filesDir.resolve(fileName).absolutePath }