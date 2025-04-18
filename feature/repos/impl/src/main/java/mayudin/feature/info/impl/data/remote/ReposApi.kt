package mayudin.feature.info.impl.data.remote

import com.squareup.anvil.annotations.ContributesBinding
import io.ktor.client.call.body
import io.ktor.client.request.get
import mayudin.common.di.SingleIn
import mayudin.common.network.KtorClientProvider
import mayudin.feature.info.impl.data.model.Repo
import mayudin.feature.repos.api.di.ReposScope
import javax.inject.Inject

interface ReposApi {
    suspend fun getUserRepos(username: String): List<Repo>
}

@SingleIn(ReposScope::class)
@ContributesBinding(ReposScope::class)
class ReposApiImpl @Inject constructor(
    private val ktor: KtorClientProvider
) : ReposApi {

    override suspend fun getUserRepos(user: String): List<Repo> {
        return ktor.client.get("https://api.github.com/users/$user/repos").body<List<Repo>>()
    }
}
