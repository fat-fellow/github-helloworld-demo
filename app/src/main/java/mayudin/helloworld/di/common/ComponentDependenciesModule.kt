package mayudin.helloworld.di.common

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import mayudin.common.di.ComponentDependencies
import mayudin.common.di.ComponentDependenciesKey
import mayudin.feature.info.impl.di.RepoDependencies
import mayudin.helloworld.di.AppComponent

@InstallIn(SingletonComponent::class)
@Module
interface ComponentDependenciesModule {

    @Binds
    @IntoMap
    @ComponentDependenciesKey(RepoDependencies::class)
    abstract fun provideAppearanceDependencies(component: AppComponent): ComponentDependencies
}