package app.lenth.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<RouteDatabase> {
  val appContext = ctx.applicationContext
  val dbFile = appContext.getDatabasePath("route.db")
  return Room.databaseBuilder<RouteDatabase>(
    context = appContext,
    name = dbFile.absolutePath
  )
}

// actual fun getRoomDatabasePlatform(builder: RoomDatabase.Builder<RouteDatabase>): RouteDatabase {
//   return builder
//     // .fallbackToDestructiveMigrationOnDowngrade(true)
//     .fallbackToDestructiveMigration(true)
//     .setDriver(AndroidSQLiteDriver())
//     .setQueryCoroutineContext(Dispatchers.IO)
//     .build()
// }