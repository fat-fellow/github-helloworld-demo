package mayudin.feature.info.impl.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mayudin.common.network.Mappers
import mayudin.feature.info.impl.data.remote.ReposApi
import mayudin.feature.info.impl.domain.repository.ReposRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReposRepositoryImpl @Inject constructor(
    private val api: ReposApi
) : ReposRepository {
    override suspend fun fetchRepos(user: String): List<String> {
        return try {
            api.getUserRepos(user).map { it.name }
        } catch (exception: Throwable) {
            throw Mappers.mapToDomain(exception)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface ReposRepositoryModule {
    @Binds
    @Singleton
    fun bindReposRepository(impl: ReposRepositoryImpl): ReposRepository
}