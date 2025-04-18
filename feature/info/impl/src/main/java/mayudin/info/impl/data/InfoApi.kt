package mayudin.info.impl.data

import com.squareup.anvil.annotations.ContributesBinding
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import mayudin.common.di.SingleIn
import mayudin.common.network.KtorClientProvider
import mayudin.info.api.di.InfoScope
import javax.inject.Inject

interface InfoApi {
    suspend fun getReposActivity(owner: String, repo: String): List<Activity>
}

@SingleIn(InfoScope::class)
@ContributesBinding(InfoScope::class)
class InfoApiImpl @Inject constructor(
    private val ktor: KtorClientProvider
) : InfoApi {

    override suspend fun getReposActivity(owner: String, repo: String): List<Activity> {
        return ktor.client.get("https://api.github.com/repos/$owner/$repo/activity")
            .body<List<Activity>>()
    }
}

@Serializable
data class Activity(
    val id: Long,
    @SerialName("activity_type") val activityType: String,
    val actor: Actor
)

@Serializable
data class Actor (
    val login: String
)