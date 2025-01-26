package app.lenth.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.sqlite.driver.AndroidSQLiteDriver
import app.lenth.data.preferences.PREFERENCE_DATA_STORE_SETTINGS
import app.lenth.data.preferences.createSharedDataStore
import app.lenth.database.RouteDatabase
import app.lenth.database.getDatabaseBuilder
import app.lenth.database.getRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val dataModule: Module = module {
    single { createDataStore(androidContext(), PREFERENCE_DATA_STORE_SETTINGS) }
    single<RouteDatabase> { getRoomDatabase(get(), AndroidSQLiteDriver()) }
    single { getDatabaseBuilder(androidContext()) }
}

fun createDataStore(context: Context, fileName: String): DataStore<Preferences> = createSharedDataStore { context.filesDir.resolve(fileName).absolutePath }