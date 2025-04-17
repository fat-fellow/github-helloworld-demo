package mayudin.common.network

import com.squareup.anvil.annotations.ContributesBinding
import io.ktor.client.HttpClient
import mayudin.common.di.AppScope
import mayudin.common.di.SingleIn
import javax.inject.Inject
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

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
