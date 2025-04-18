package mayudin.feature.info.impl.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Repo(
    val id: Long,
    val name: String,
)