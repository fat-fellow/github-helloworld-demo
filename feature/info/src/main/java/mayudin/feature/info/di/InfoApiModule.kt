package mayudin.feature.info.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import mayudin.feature.info.data.remote.InfoApi
import mayudin.feature.info.data.remote.InfoApiImpl

@Module
@InstallIn(SingletonComponent::class)
interface InfoApiModule {
    @Binds
    @Singleton
    fun bindInfoApi(impl: InfoApiImpl): InfoApi
}
