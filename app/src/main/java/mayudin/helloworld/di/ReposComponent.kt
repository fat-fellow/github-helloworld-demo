package mayudin.helloworld.di

import com.squareup.anvil.annotations.MergeComponent
import dagger.Component
import mayudin.common.di.AppScope
import mayudin.common.di.SingleIn
import mayudin.repos.api.di.ReposScope
import mayudin.repos.api.presentation.viewmodel.ReposViewModelFactory
import mayudin.repos.impl.di.RepoDependencies


@SingleIn(ReposScope::class)
@MergeComponent(
    scope = ReposScope::class,
    dependencies = [RepoDependencies::class]
)
interface ReposComponent {
    val viewModelFactory: ReposViewModelFactory
}