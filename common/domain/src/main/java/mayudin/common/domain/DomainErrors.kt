package mayudin.common.domain

sealed class DomainError(cause: Throwable?) : Throwable(cause) {

    sealed class Network(cause: Throwable?) : DomainError(cause) {
        object NoConnection : Network(null)
        object Timeout : Network(null)
        data class Server(val code: Int, override val cause: Throwable?) : Network(cause)
    }

    data class Unknown(override val cause: Throwable?) : DomainError(cause)
}
