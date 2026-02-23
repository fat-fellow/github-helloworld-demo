package mayudin.feature.info.data.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton
import mayudin.common.network.KtorClientProvider
import mayudin.feature.info.data.model.RepoActivity

interface InfoApi {
    suspend fun getReposActivity(owner: String, repo: String): List<RepoActivity>
}

@Singleton
class InfoApiImpl @Inject constructor(private val ktor: KtorClientProvider) : InfoApi {

    override suspend fun getReposActivity(owner: String, repo: String): List<RepoActivity> {
        return ktor.client.get("https://api.github.com/repos/$owner/$repo/activity")
            .body<List<RepoActivity>>()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface InfoApiModule {
    @Binds
    @Singleton
    fun bindInfoApi(impl: InfoApiImpl): InfoApi
}
