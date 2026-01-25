package mayudin.feature.repos.domain.usecase

interface ReposUseCase {
    suspend operator fun invoke(params: String): List<String>
}
