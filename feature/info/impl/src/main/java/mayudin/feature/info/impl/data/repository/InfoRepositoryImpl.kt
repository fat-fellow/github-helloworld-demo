package mayudin.feature.info.impl.data.repository

import com.squareup.anvil.annotations.ContributesBinding
import mayudin.common.network.Mappers
import mayudin.feature.info.api.di.InfoScope
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.impl.data.remote.InfoApi
import mayudin.feature.info.impl.domain.repository.InfoRepository
import javax.inject.Inject

@ContributesBinding(InfoScope::class)
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