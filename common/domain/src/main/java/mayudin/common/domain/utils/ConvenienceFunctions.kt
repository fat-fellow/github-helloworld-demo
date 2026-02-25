package mayudin.common.domain.utils

import kotlin.coroutines.cancellation.CancellationException

/*
 Catches all exceptions except CancellationException and Error, which are rethrown.
 The onError lambda is called with the caught exception and its result is returned.
 */
suspend inline fun <T> (suspend () -> T).tryCatching(
    crossinline onError: (Throwable) -> T,
): T = try {
    invoke()
} catch (t: Throwable) { // some people create exceptions by extending Throwable
    // Never intercept cancellation or serious JVM errors
    if (t is CancellationException || t is Error) throw t
    onError(t)
}


inline fun <T> (() -> T).tryCatching(
    crossinline onError: (Throwable) -> T,
): T = try {
    invoke()
} catch (t: Throwable) { // some people create exceptions by extending Throwable
    // Never intercept cancellation or serious JVM errors
    if (t is CancellationException || t is Error) throw t
    onError(t)
}
