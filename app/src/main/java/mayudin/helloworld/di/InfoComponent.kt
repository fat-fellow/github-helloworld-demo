package mayudin.helloworld.di

import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent
import mayudin.common.di.SingleIn
import mayudin.info.api.di.InfoScope
import mayudin.info.api.domain.model.GitHubRepository
import mayudin.info.api.presentation.viewmodel.InfoViewModelFactory
import mayudin.repos.api.presentation.viewmodel.ReposViewModelFactory

@SingleIn(InfoScope::class)
@MergeSubcomponent(InfoScope::class)
interface InfoComponent {

    val viewModelFactory: InfoViewModelFactory

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun repo(gitHubRepository: GitHubRepository) : Builder
        fun build(): InfoComponent
    }
}