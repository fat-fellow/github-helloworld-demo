package mayudin.helloworld.di

import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import dagger.hilt.InstallIn
import mayudin.feature.info.api.di.InfoScope
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.presentation.viewmodel.InfoViewModelFactory

@InstallIn(InfoScope::class)
@Module
interface InfoComponent {

    val viewModelFactory: InfoViewModelFactory

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun repo(gitHubRepo: GitHubRepo): Builder
        fun build(): InfoComponent
    }
}