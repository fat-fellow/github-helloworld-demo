package mayudin.feature.info.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import mayudin.common.network.util.safeRequestRun
import mayudin.feature.info.data.remote.InfoApi
import mayudin.feature.info.domain.model.GitHubRepo
import mayudin.feature.info.domain.repository.InfoRepository

@Singleton
class InfoRepositoryImpl @Inject constructor(private val api: InfoApi) : InfoRepository {

    override suspend fun getReposActivity(repo: GitHubRepo) =
        safeRequestRun { api.getReposActivity(repo.owner, repo.repo).map { it.activityType } }
}

@Module
@InstallIn(SingletonComponent::class)
interface InfoRepositoryModule {
    @Binds
    @Singleton
    fun bindInfoRepository(impl: InfoRepositoryImpl): InfoRepository
}