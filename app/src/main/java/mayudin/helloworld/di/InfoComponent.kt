package mayudin.helloworld.di

import com.squareup.anvil.annotations.MergeSubcomponent
import dagger.BindsInstance
import dagger.Subcomponent
import mayudin.common.di.SingleIn
import mayudin.feature.info.api.di.InfoScope
import mayudin.feature.info.api.domain.model.GitHubRepo
import mayudin.feature.info.api.presentation.viewmodel.InfoViewModelFactory

@SingleIn(InfoScope::class)
@MergeSubcomponent(InfoScope::class)
interface InfoComponent {

    val viewModelFactory: InfoViewModelFactory

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun repo(gitHubRepo: GitHubRepo): Builder
        fun build(): InfoComponent
    }
}