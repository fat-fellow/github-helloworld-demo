package mayudin.feature.info.presentation.model

sealed interface InfoEffect {
    data object NavigateBack : InfoEffect
}