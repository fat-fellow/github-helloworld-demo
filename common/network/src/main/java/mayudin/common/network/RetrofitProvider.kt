package mayudin.common.network

import com.squareup.anvil.annotations.ContributesBinding
import kotlinx.serialization.json.Json
import mayudin.common.di.AppScope
import mayudin.common.di.SingleIn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject

interface RetrofitProvider {
    fun <T> create(service: Class<T>): T
}

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class RetrofitProviderImpl @Inject constructor(): RetrofitProvider {
    private val retrofit by lazy {
        val contentType: MediaType = "application/json".toMediaType()
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging) // 👈 вот сюда
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .build()
    }

    override fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }
}