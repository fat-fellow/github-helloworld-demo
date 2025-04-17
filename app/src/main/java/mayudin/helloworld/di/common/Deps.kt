package mayudin.helloworld.di.common

import com.squareup.anvil.annotations.ContributesTo
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds
import mayudin.common.di.AppScope
import mayudin.common.di.ComponentDependencies
import mayudin.common.di.ComponentDependenciesKey
import mayudin.helloworld.di.AppComponent
import mayudin.repos.impl.di.RepoDependencies

@ContributesTo(AppScope::class)
@Module
interface ComponentDependenciesModule {

    @Binds
    @IntoMap
    @ComponentDependenciesKey(RepoDependencies::class)
    abstract fun provideAppearanceDependencies(component: AppComponent): ComponentDependencies
}