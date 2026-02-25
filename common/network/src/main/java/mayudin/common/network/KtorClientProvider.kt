package mayudin.common.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min
import kotlin.random.Random

@Singleton
class KtorClientProviderImpl @Inject constructor() : KtorClientProvider {
    override val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpRequestRetry) {
            maxRetries = 3

            retryOnServerErrors()

            //jittered exponential backoff with a cap of 10 seconds
            delayMillis { retry ->
                val cap = 10_000L // 10 seconds
                val base = 1000L * (1 shl retry)
                val maxDelay = min(base, cap)
                Random.nextLong(0, maxDelay)
            }
        }
    }
}

interface KtorClientProvider {
    val client: HttpClient
}

@Module
@InstallIn(SingletonComponent::class)
interface KtorClientProviderModule {
    @Binds
    @Singleton
    fun bindKtorClientProvider(impl: KtorClientProviderImpl): KtorClientProvider
}
