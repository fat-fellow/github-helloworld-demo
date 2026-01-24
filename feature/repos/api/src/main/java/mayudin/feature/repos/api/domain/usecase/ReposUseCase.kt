package mayudin.feature.repos.api.domain.usecase

interface ReposUseCase {
    suspend operator fun invoke(params: String): List<String>
}
