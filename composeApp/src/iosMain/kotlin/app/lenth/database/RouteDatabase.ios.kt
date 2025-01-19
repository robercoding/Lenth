package app.lenth.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.lenth.database.RouteDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabaseBuilder(): RoomDatabase.Builder<RouteDatabase> {
    val dbFilePath = documentDirectory() + "/my_room.db"
    return Room.databaseBuilder<RouteDatabase>(
        name = dbFilePath,
    )
}
//
// actual fun getRoomDatabasePlatform(builder: RoomDatabase.Builder<RouteDatabase>): RouteDatabase {
//     return builder
//         // .fallbackToDestructiveMigrationOnDowngrade(true)
//         .fallbackToDestructiveMigration(true)
//         .setDriver(BundledSQLiteDriver())
//         .setQueryCoroutineContext(Dispatchers.IO)
//         .build()
// }

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
  val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
    directory = NSDocumentDirectory,
    inDomain = NSUserDomainMask,
    appropriateForURL = null,
    create = false,
    error = null,
  )
  return requireNotNull(documentDirectory?.path)
}