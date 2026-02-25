package mayudin.feature.repos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [RepoEntity::class], version = 1, exportSchema = false)
abstract class ReposDatabase : RoomDatabase() {
    abstract fun reposDao(): ReposDao
}

@Module
@InstallIn(SingletonComponent::class)
object ReposDatabaseModule {
    @Provides
    @Singleton
    fun provideReposDatabase(@ApplicationContext context: Context): ReposDatabase =
        Room.databaseBuilder(context, ReposDatabase::class.java, "repos_database")
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    @Singleton
    fun provideReposDao(database: ReposDatabase): ReposDao = database.reposDao()
}
