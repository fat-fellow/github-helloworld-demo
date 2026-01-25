package mayudin.feature.info.api.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data class InfoRoute(
    val owner: String,
    val repo: String
)
