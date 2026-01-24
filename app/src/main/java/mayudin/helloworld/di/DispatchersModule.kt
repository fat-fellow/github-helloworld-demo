package mayudin.helloworld.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Singleton
    @Dispatcher(CoroutinesDispatchers.IO)
    fun providesIODispatcher(): CoroutineContext = Dispatchers.IO

    @Provides
    @Singleton
    @Dispatcher(CoroutinesDispatchers.Default)
    fun providesDefaultDispatcher(): CoroutineContext = Dispatchers.Default
}
