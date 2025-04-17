package mayudin.helloworld.di.common

import mayudin.common.di.ComponentDependencies
import mayudin.common.di.HasComponentDependencies
import mayudin.helloworld.di.AppComponent
import mayudin.helloworld.di.DaggerReposComponent

class ComponentManager(
    private val main: AppComponent,
    private val provider: HasComponentDependencies
) {

    val repoComponent = Component {
        DaggerReposComponent.builder()
            .repoDependencies(findComponentDependencies())
            .build()
    }

    class Component<T>(private val builder: () -> T) {

        private var instance: T? = null

        fun get() = instance ?: builder().also { instance = it }

        fun new() = builder().also { instance = it }

        fun release() {
            instance = null
        }

        fun isInitialized() = instance != null
    }


    private inline fun <reified T : ComponentDependencies> findComponentDependencies(): T {
        return provider.dependencies[T::class.java] as T
    }
}