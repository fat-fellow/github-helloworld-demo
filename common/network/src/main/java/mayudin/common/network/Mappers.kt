package mayudin.common.network

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.*
import mayudin.common.domain.DomainError

object Mappers {
    fun mapToDomain(exception: Throwable): DomainError {
        return when (exception) {
            is HttpRequestTimeoutException, is ConnectTimeoutException -> DomainError.Network.Timeout
            is ServerResponseException, is ClientRequestException, is RedirectResponseException -> {
                DomainError.Network.Server(
                    code = (exception as ResponseException).response.status.value,
                    cause = exception
                )
            }
            is NoTransformationFoundException -> DomainError.Network.NoConnection
            else -> DomainError.Unknown(exception)
        }
    }
}
