package mayudin.repos.impl.data

import com.squareup.anvil.annotations.ContributesBinding
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import retrofit2.http.GET
import retrofit2.http.Path
import kotlinx.serialization.Serializable
import mayudin.common.di.SingleIn
import mayudin.common.network.KtorClientProvider
import mayudin.repos.api.di.ReposScope
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

@Serializable
data class Repo(
    val id: Long,
    val name: String,
    val description: String?,
    @SerialName("html_url") val htmlUrl: String
)
