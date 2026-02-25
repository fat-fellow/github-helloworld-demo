package mayudin.feature.repos.data.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import mayudin.common.network.util.tryCatching
import mayudin.feature.repos.data.api.ReposApi
import mayudin.feature.repos.domain.repository.ReposRepository

@Singleton
class ReposRepositoryImpl @Inject constructor(private val api: ReposApi) : ReposRepository {
    override suspend fun fetchRepos(user: String) = tryCatching { api.getUserRepos(user).map { it.name } }
}

@Module
@InstallIn(SingletonComponent::class)
interface ReposRepositoryModule {
    @Binds
    @Singleton
    fun bindReposRepository(impl: ReposRepositoryImpl): ReposRepository
}
