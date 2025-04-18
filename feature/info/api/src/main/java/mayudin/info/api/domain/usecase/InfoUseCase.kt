package mayudin.info.api.domain.usecase

import mayudin.common.utils.domain.ResultUseCase
import mayudin.info.api.domain.model.GitHubRepository

interface InfoUseCase : ResultUseCase<GitHubRepository, List<String>>