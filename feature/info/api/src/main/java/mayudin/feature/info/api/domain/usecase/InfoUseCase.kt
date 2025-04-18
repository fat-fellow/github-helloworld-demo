package mayudin.feature.info.api.domain.usecase

import mayudin.common.utils.domain.ResultUseCase
import mayudin.feature.info.api.domain.model.GitHubRepo

interface InfoUseCase : ResultUseCase<GitHubRepo, List<String>>