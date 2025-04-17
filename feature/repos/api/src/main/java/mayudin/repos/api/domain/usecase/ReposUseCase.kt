package mayudin.repos.api.domain.usecase

import mayudin.common.utils.domain.ResultUseCase

interface ReposUseCase : ResultUseCase <String, List<String>>