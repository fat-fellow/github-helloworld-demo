package mayudin.helloworld.di

import dagger.hilt.InstallIn
import mayudin.common.di.SingleIn
import mayudin.feature.info.impl.di.RepoDependencies
import mayudin.feature.repos.api.di.ReposScope
import mayudin.feature.repos.api.presentation.viewmodel.ReposViewModelFactory

@SingleIn(ReposScope::class)
@InstallIn(ReposScope::class)
interface ReposComponent {
    val viewModelFactory: ReposViewModelFactory
}