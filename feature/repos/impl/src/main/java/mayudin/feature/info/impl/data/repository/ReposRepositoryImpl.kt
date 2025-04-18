package mayudin.feature.info.impl.data.repository

import com.squareup.anvil.annotations.ContributesBinding
import mayudin.common.network.Mappers
import mayudin.feature.info.impl.data.remote.ReposApi
import mayudin.feature.info.impl.domain.repository.ReposRepository
import mayudin.feature.repos.api.di.ReposScope
import javax.inject.Inject

@ContributesBinding(ReposScope::class)
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