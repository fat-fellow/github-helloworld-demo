package mayudin.feature.info.impl.data.remote

import com.squareup.anvil.annotations.ContributesBinding
import io.ktor.client.call.body
import io.ktor.client.request.get
import mayudin.common.di.SingleIn
import mayudin.common.network.KtorClientProvider
import mayudin.feature.info.api.di.InfoScope
import mayudin.feature.info.impl.data.model.Activity
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