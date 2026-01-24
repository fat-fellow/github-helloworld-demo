package mayudin.helloworld.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mayudin.feature.info.impl.di.RepoDependencies
import mayudin.helloworld.MainActivity

@InstallIn(SingletonComponent::class)
@EntryPoint
interface AppComponent : RepoDependencies {

    fun infoComponentBuilder(): InfoComponent.Builder

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}