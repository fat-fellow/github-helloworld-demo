package mayudin.common.network.util

import mayudin.common.domain.utils.tryCatching
import mayudin.common.network.Mappers

suspend fun <T> tryCatching(block: suspend () -> T): T {
    return block.tryCatching { throw Mappers.mapToDomain(it) }
}
