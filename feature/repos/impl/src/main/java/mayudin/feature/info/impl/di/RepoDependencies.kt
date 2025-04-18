package mayudin.feature.info.impl.di

import mayudin.common.di.ComponentDependencies
import mayudin.common.di.dispatchers.CoroutinesDispatchers
import mayudin.common.di.dispatchers.Dispatcher
import mayudin.common.network.KtorClientProvider
import kotlin.coroutines.CoroutineContext

interface RepoDependencies : ComponentDependencies {
    fun ktorProvider(): KtorClientProvider
    @Dispatcher(CoroutinesDispatchers.IO) fun ioCoroutineContext(): CoroutineContext
}