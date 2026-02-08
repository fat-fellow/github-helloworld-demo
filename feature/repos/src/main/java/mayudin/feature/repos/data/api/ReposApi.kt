package mayudin.feature.repos.data.api

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton
import mayudin.common.network.KtorClientProvider
import mayudin.feature.repos.data.model.Repo

interface ReposApi {
    suspend fun getUserRepos(user: String): List<Repo>
}

@Singleton
class ReposApiImpl @Inject constructor(private val ktor: KtorClientProvider) : ReposApi {

    override suspend fun getUserRepos(user: String): List<Repo> {
        return ktor.client.get("https://api.github.com/users/$user/repos").body<List<Repo>>()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface ReposApiModule {
    @Binds
    @Singleton
    fun bindReposApi(impl: ReposApiImpl): ReposApi
}