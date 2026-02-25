package mayudin.feature.repos.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import mayudin.feature.repos.data.local.ReposDao
import mayudin.feature.repos.data.local.ReposDatabase

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
