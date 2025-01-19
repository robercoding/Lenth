package app.lenth.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.lenth.data.db.converter.RouteListConverter
import app.lenth.data.db.dao.RouteDao
import app.lenth.data.db.models.OptimalRouteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(entities = [OptimalRouteEntity::class], version = 1, exportSchema = true)
@TypeConverters(RouteListConverter::class)
@ConstructedBy(RouteDatabaseConstructor::class)
abstract class RouteDatabase : RoomDatabase() {
  abstract fun getRouteDao(): RouteDao
}


// The Room compiler generates the `actual` implementations. https://developer.android.com/kotlin/multiplatform/room#defining-database
@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object RouteDatabaseConstructor : RoomDatabaseConstructor<RouteDatabase> {
  override fun initialize(): RouteDatabase
}

// To inject AndroidSQLDriver to be able to open IDE Database Inspector feature. With BundledSQLDriver the Database looks closed on DatabaseInspector.
// expect fun getRoomDatabasePlatform(builder: RoomDatabase.Builder<RouteDatabase>): RouteDatabase
fun getRoomDatabase(
  builder: RoomDatabase.Builder<RouteDatabase>,
  sqLiteDriver: SQLiteDriver,
): RouteDatabase {
  return builder
    // .fallbackToDestructiveMigrationOnDowngrade(true)
    .fallbackToDestructiveMigration(true)
    .setDriver(sqLiteDriver)
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
}