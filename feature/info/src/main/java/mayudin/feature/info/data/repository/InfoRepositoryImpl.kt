package mayudin.feature.info.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import mayudin.common.network.util.tryCatching
import mayudin.feature.info.data.remote.InfoApi
import mayudin.feature.info.domain.model.GitHubRepo
import mayudin.feature.info.domain.repository.InfoRepository

@Singleton
class InfoRepositoryImpl @Inject constructor(private val api: InfoApi) : InfoRepository {

    override suspend fun getReposActivity(repo: GitHubRepo) =
        tryCatching { api.getReposActivity(repo.owner, repo.repo).map { it.activityType } }
}
