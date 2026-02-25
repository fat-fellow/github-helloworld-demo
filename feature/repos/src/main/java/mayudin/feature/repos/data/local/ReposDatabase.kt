package mayudin.feature.repos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RepoEntity::class, RepoSyncMetadata::class], version = 2, exportSchema = false)
abstract class ReposDatabase : RoomDatabase() {
    abstract fun reposDao(): ReposDao
}
