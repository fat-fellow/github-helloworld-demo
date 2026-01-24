package mayudin.feature.info.impl.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mayudin.common.network.Mappers
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.impl.data.remote.InfoApi
import mayudin.feature.info.impl.domain.repository.InfoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InfoRepositoryImpl @Inject constructor(
    private val api: InfoApi
) : InfoRepository {

    override suspend fun getReposActivity(repo: GitHubRepo): List<String> {
        return try {
            api.getReposActivity(repo.owner, repo.repo).map { it.activityType }
        } catch (exception: Throwable) {
            throw Mappers.mapToDomain(exception)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface InfoRepositoryModule {
    @Binds
    @Singleton
    fun bindInfoRepository(impl: InfoRepositoryImpl): InfoRepository
}

