package mayudin.helloworld.di

import android.content.Context
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import mayudin.common.di.AppScope
import mayudin.common.di.SingleIn
import mayudin.feature.info.impl.di.RepoDependencies
import mayudin.helloworld.MainActivity

@SingleIn(AppScope::class)
@MergeComponent(AppScope::class)
interface AppComponent : RepoDependencies {

    fun infoComponentBuilder(): InfoComponent.Builder

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}