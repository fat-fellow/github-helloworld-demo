package mayudin.repos.impl.data

import com.squareup.anvil.annotations.ContributesBinding
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.repos.api.di.ReposScope
import mayudin.repos.impl.domain.repository.ReposRepository
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ContributesBinding(ReposScope::class)
class ReposRepositoryImpl @Inject constructor(
    private val api: ReposApi
) : ReposRepository {
    override suspend fun fetchRepos(user: String): List<String> {
        return api.getUserRepos(user).map { it.name }
    }
}