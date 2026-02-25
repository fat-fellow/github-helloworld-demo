package mayudin.feature.info.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import mayudin.feature.info.data.repository.InfoRepositoryImpl
import mayudin.feature.info.domain.repository.InfoRepository

@Module
@InstallIn(SingletonComponent::class)
interface InfoRepositoryModule {
    @Binds
    @Singleton
    fun bindInfoRepository(impl: InfoRepositoryImpl): InfoRepository
}
