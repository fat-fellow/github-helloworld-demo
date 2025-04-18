package mayudin.helloworld.di.common

import mayudin.common.di.ComponentDependencies
import mayudin.common.di.HasComponentDependencies
import mayudin.helloworld.di.AppComponent
import mayudin.helloworld.di.DaggerReposComponent
import java.lang.ref.WeakReference

class ComponentManager(
    val main: AppComponent,
    private val provider: HasComponentDependencies
) {

    val repoComponent = Component {
        DaggerReposComponent.builder()
            .repoDependencies(findComponentDependencies())
            .build()
    }

    class Component<T>(private val builder: () -> T) {

        private var instance: WeakReference<T>? = null

        fun get(): T = instance?.get() ?: builder().also { instance = WeakReference(it) }

        fun new(): T = builder().also { instance = WeakReference(it) }

        fun release() {
            instance = null
        }

        fun isInitialized() = instance != null && instance?.get() != null
    }


    private inline fun <reified T : ComponentDependencies> findComponentDependencies(): T {
        return provider.dependencies[T::class.java] as T
    }
}