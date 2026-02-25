package mayudin.feature.repos.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.data.api.ReposApiImpl

@Module
@InstallIn(SingletonComponent::class)
interface ReposApiModule {
    @Binds
    @Singleton
    fun bindReposApi(impl: ReposApiImpl): ReposApi
}
