package mayudin.common.di

import dagger.MapKey
import kotlin.reflect.KClass

interface ComponentDependencies

typealias ComponentDependenciesProvider = Map<Class<out ComponentDependencies>,
        @JvmSuppressWildcards ComponentDependencies>

interface HasComponentDependencies {
    val dependencies: ComponentDependenciesProvider
}

@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ComponentDependenciesKey(val value: KClass<out ComponentDependencies>)