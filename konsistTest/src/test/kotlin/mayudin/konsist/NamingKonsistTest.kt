package mayudin.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

/**
 * Enforces naming conventions across all feature modules:
 * - ViewModels live in presentation.viewmodel and end with ViewModel
 * - UseCases live in domain.usecase and end with UseCase
 * - Repository implementations live in data.repository and end with RepositoryImpl
 * - Repository interfaces live in domain.repository and end with Repository
 * - Effect sealed interfaces live in presentation.model and end with Effect
 * - UiState sealed interfaces live in presentation.model and are named UiState
 */
class NamingKonsistTest {

    private val scope = Konsist.scopeFromProject(sourceSetName = "main")

    @Test
    fun `ViewModel classes reside in presentation viewmodel package`() {
        scope
            .classes()
            .withNameEndingWith("ViewModel")
            .assertTrue { it.resideInPackage("..presentation.viewmodel") }
    }

    @Test
    fun `classes in presentation viewmodel package have ViewModel suffix`() {
        scope
            .classes()
            .filter { it.resideInPackage("..presentation.viewmodel") }
            .assertTrue { it.name.endsWith("ViewModel") }
    }

    @Test
    fun `UseCase classes reside in domain usecase package`() {
        scope
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { it.resideInPackage("..domain.usecase") }
    }

    @Test
    fun `classes in domain usecase package have UseCase suffix`() {
        scope
            .classes()
            .filter { it.resideInPackage("..domain.usecase") }
            .assertTrue { it.name.endsWith("UseCase") }
    }

    @Test
    fun `Repository implementation classes reside in data repository package`() {
        scope
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { it.resideInPackage("..data.repository") }
    }

    @Test
    fun `Repository interfaces reside in domain repository package`() {
        scope
            .interfaces()
            .withNameEndingWith("Repository")
            .assertTrue { it.resideInPackage("..domain.repository") }
    }

    @Test
    fun `Effect sealed interfaces reside in presentation model package`() {
        scope
            .interfaces()
            .withNameEndingWith("Effect")
            .assertTrue { it.resideInPackage("..presentation.model") }
    }

    @Test
    fun `UiState sealed interfaces reside in presentation model package`() {
        scope
            .interfaces()
            .filter { it.name == "UiState" }
            .assertTrue { it.resideInPackage("..presentation.model") }
    }

    @Test
    fun `navigation route files reside in presentation navigation package`() {
        scope
            .classes()
            .filter { it.name.endsWith("Route") }
            .assertTrue { it.resideInPackage("..presentation.navigation") }
    }
}

