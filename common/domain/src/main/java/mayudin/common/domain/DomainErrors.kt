package mayudin.common.domain

sealed class DomainError(cause: Throwable?, override val message: String?) : Throwable(cause) {

    sealed class Network(cause: Throwable?) : DomainError(cause, UnknownError) {
        class NoConnection : Network(null)
        class Timeout : Network(null)
        data class Server(val code: Int, override val cause: Throwable?) : Network(cause)
    }

    data class Unknown(override val cause: Throwable?) : DomainError(cause, UnknownError)
}

private const val UnknownError = "Unknown error occurred"