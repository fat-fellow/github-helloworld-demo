package mayudin.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.ext.list.withParentClassNamed
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

/**
 * Enforces annotation discipline:
 * - Every class that extends ViewModel is annotated with @HiltViewModel
 * - Every RepositoryImpl is annotated with @Singleton
 * - Every UseCase has an @Inject constructor (or is @Singleton-scoped)
 * - Navigation route data classes and objects are @Serializable
 * - UiState nested states are @Immutable
 */
class AnnotationKonsistTest {

    private val scope = Konsist.scopeFromProject()

    @Test
    fun `ViewModel subclasses are annotated with HiltViewModel`() {
        scope
            .classes()
            .withParentClassNamed("ViewModel")
            .assertTrue { it.hasAnnotationWithName("HiltViewModel") }
    }

    @Test
    fun `Repository implementation classes are annotated with Singleton`() {
        scope
            .classes()
            .withNameEndingWith("RepositoryImpl")
            .assertTrue { it.hasAnnotationWithName("Singleton") }
    }

    @Test
    fun `UseCase classes have an Inject constructor`() {
        scope
            .classes()
            .withNameEndingWith("UseCase")
            .assertTrue { useCase ->
                useCase.constructors.any { ctor ->
                    ctor.hasAnnotationWithName("Inject")
                }
            }
    }

    @Test
    fun `navigation route classes are annotated with Serializable`() {
        scope
            .classes()
            .filter { it.resideInPackage("..presentation.navigation") }
            .assertTrue { it.hasAnnotationWithName("Serializable") }
    }

    @Test
    fun `navigation route objects are annotated with Serializable`() {
        scope
            .objects()
            .filter { it.resideInPackage("..presentation.navigation") }
            .assertTrue { it.hasAnnotationWithName("Serializable") }
    }

    @Test
    fun `UiState nested classes are annotated with Immutable`() {
        scope
            .classes()
            .withNameEndingWith("UiState")
            .filter { it.resideInPackage("..presentation.model") }
            .assertTrue { it.hasAnnotationWithName("Immutable") }
    }
}

