package mayudin.helloworld.di

import android.content.Context
import com.squareup.anvil.annotations.MergeComponent
import dagger.BindsInstance
import dagger.Component
import mayudin.common.di.AppScope
import mayudin.common.di.SingleIn
import mayudin.helloworld.MainActivity
import mayudin.info.impl.di.RepoDependencies

@SingleIn(AppScope::class)
@MergeComponent(AppScope::class)
interface AppComponent : RepoDependencies {

    fun infoComponentBuilder(): InfoComponent.Builder

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }

    fun inject(mainActivity: MainActivity)
}