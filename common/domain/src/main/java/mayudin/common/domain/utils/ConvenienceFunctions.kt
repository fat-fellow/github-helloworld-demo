package mayudin.common.domain.utils

import kotlin.coroutines.cancellation.CancellationException

// Catches all exceptions except CancellationException and Error, which are rethrown.
// The onError lambda is called with the caught exception and its result is returned.
suspend inline fun <T> safeRun(
    crossinline block: suspend () -> T,
    crossinline onError: (Throwable) -> T,
): T {
    return try {
        block()
    } catch (t: Throwable) {

        if (t is CancellationException || t is Error) {
            throw t
        }

        onError(t)
    }
}