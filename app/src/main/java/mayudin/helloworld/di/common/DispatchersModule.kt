package mayudin.helloworld.di.common

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import mayudin.common.di.AppScope
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher

@ContributesTo(AppScope::class)
@Module
object DispatchersModule {
    @Provides
    @Dispatcher(CoroutinesDispatchers.IO)
    fun providesIODispatcher(): CoroutineContext = Dispatchers.IO

    @Provides
    @Dispatcher(CoroutinesDispatchers.Default)
    fun providesDefaultDispatcher(): CoroutineContext = Dispatchers.Default
}