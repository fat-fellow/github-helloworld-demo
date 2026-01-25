package mayudin.feature.repos.presentation.model

sealed interface RepoEffect {
    data class NavigateToInfo(val owner: String, val repo: String) : RepoEffect
}
