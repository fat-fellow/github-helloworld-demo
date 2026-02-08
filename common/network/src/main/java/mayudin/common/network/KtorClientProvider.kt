package mayudin.common.network

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Singleton
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

@Module
@InstallIn(SingletonComponent::class)
interface KtorClientProviderModule {
    @Binds
    @Singleton
    fun bindKtorClientProvider(impl: KtorClientProviderImpl): KtorClientProvider
}