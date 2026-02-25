package mayudin.feature.repos.data.repository

import android.util.Log
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import mayudin.common.network.util.tryCatching
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.data.local.RepoEntity
import mayudin.feature.repos.data.local.ReposDao
import mayudin.feature.repos.domain.repository.ReposRepository

@Singleton
class ReposRepositoryImpl @Inject constructor(
    private val api: ReposApi,
    private val reposDao: ReposDao,
) : ReposRepository {

    override fun fetchRepos(user: String): Flow<List<String>> =
        reposDao.observeRepos(user)
            .map { entities ->
                entities.map { it.name } }
            .onStart {
                tryCatching {
                    val fresh = api.getUserRepos(user)
                    reposDao.replaceRepos(
                        user,
                        fresh.map { RepoEntity(ownerId = user, name = it.name) }
                    )
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
