package mayudin.common.network

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.util.network.UnresolvedAddressException
import mayudin.common.domain.DomainError

object Mappers {
    fun mapToDomain(exception: Throwable): DomainError {
        return when (exception) {
            is HttpRequestTimeoutException, is ConnectTimeoutException -> DomainError.Network.Timeout()
            is ServerResponseException, is ClientRequestException, is RedirectResponseException -> {
                DomainError.Network.Server(
                    code = exception.response.status.value,
                    cause = exception,
                )
            }

            is NoTransformationFoundException, is UnresolvedAddressException -> DomainError.Network.NoConnection()
            else -> DomainError.Unknown(exception)
        }
    }
}
