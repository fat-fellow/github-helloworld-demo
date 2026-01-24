package mayudin.helloworld.di

import com.squareup.anvil.annotations.MergeComponent
import mayudin.common.di.SingleIn
import mayudin.feature.info.impl.di.RepoDependencies
import mayudin.feature.repos.api.di.ReposScope
import mayudin.feature.repos.api.presentation.viewmodel.ReposViewModelFactory

@SingleIn(ReposScope::class)
@MergeComponent(
    scope = ReposScope::class,
    dependencies = [RepoDependencies::class],
)
interface ReposComponent {
    val viewModelFactory: ReposViewModelFactory
}