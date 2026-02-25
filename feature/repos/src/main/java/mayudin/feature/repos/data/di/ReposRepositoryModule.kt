package mayudin.feature.repos.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import mayudin.feature.repos.data.repository.ReposRepositoryImpl
import mayudin.feature.repos.domain.repository.ReposRepository

@Module
@InstallIn(SingletonComponent::class)
interface ReposRepositoryModule {
    @Binds
    @Singleton
    fun bindReposRepository(impl: ReposRepositoryImpl): ReposRepository
}
