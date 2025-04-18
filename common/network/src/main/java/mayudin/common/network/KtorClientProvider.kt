package mayudin.common.network

import com.squareup.anvil.annotations.ContributesBinding
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import mayudin.common.di.AppScope
import mayudin.common.di.SingleIn
import javax.inject.Inject

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class KtorClientProviderImpl @Inject constructor() : KtorClientProvider {
    override val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}

interface KtorClientProvider {
    val client: HttpClient
}
