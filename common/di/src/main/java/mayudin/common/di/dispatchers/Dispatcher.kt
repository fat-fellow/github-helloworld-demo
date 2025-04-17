package mayudin.common.di.dispatchers

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val niaDispatcher: CoroutinesDispatchers)

enum class CoroutinesDispatchers {
    Default,
    IO,
}