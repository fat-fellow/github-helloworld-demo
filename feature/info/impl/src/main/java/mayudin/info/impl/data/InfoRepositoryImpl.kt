package mayudin.info.impl.data

import com.squareup.anvil.annotations.ContributesBinding
import mayudin.common.network.Mappers
import mayudin.info.api.di.InfoScope
import mayudin.info.api.domain.model.GitHubRepository
import mayudin.info.impl.domain.repository.InfoRepository
import javax.inject.Inject

@ContributesBinding(InfoScope::class)
class InfoRepositoryImpl @Inject constructor(
    private val api: InfoApi
) : InfoRepository {

    override suspend fun getReposActivity(repo: GitHubRepository): List<String> {
        return try {
            api.getReposActivity(repo.owner, repo.repo).map { it.activityType }
        } catch (exception: Throwable) {
            throw Mappers.mapToDomain(exception)
        }
    }
}
