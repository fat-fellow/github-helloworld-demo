package mayudin.info.impl.data

import com.squareup.anvil.annotations.ContributesBinding
import mayudin.common.network.Mappers
import mayudin.repos.api.di.ReposScope
import mayudin.info.impl.domain.repository.ReposRepository
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
