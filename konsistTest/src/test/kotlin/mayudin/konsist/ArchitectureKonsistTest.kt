package mayudin.konsist

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertFalse
import org.junit.Test

/**
 * Enforces Clean Architecture layer isolation:
 * - domain must not depend on data or presentation
 * - data must not depend on presentation
 *
 * Allowed directions:
 *   presentation → domain ← (nothing)
 *   data         → domain ← (nothing)
 */
class ArchitectureKonsistTest {

    private val scope = Konsist.scopeFromProject()

    @Test
    fun `domain layer does not depend on data layer`() {
        scope
            .files
            .filter { file ->
                val pkg = file.packagee?.name ?: return@filter false
                ".domain." in pkg || pkg.endsWith(".domain")
            }
            .assertFalse(additionalMessage = "Domain layer must not import from the data layer") { file ->
                file.imports.any { ".data." in it.name }
            }
    }

    @Test
    fun `domain layer does not depend on presentation layer`() {
        scope
            .files
            .filter { file ->
                val pkg = file.packagee?.name ?: return@filter false
                ".domain." in pkg || pkg.endsWith(".domain")
            }
            .assertFalse(additionalMessage = "Domain layer must not import from the presentation layer") { file ->
                file.imports.any { ".presentation." in it.name }
            }
    }

    @Test
    fun `data layer does not depend on presentation layer`() {
        scope
            .files
            .filter { file ->
                val pkg = file.packagee?.name ?: return@filter false
                ".data." in pkg || pkg.endsWith(".data")
            }
            .assertFalse(additionalMessage = "Data layer must not import from the presentation layer") { file ->
                file.imports.any { ".presentation." in it.name }
            }
    }

    @Test
    fun `common domain module does not depend on feature modules`() {
        scope
            .files
            .filter { file ->
                val pkg = file.packagee?.name ?: return@filter false
                pkg.startsWith("mayudin.common.domain")
            }
            .assertFalse(additionalMessage = "Common domain must not import from feature modules") { file ->
                file.imports.any { "mayudin.feature." in it.name }
            }
    }

    @Test
    fun `common network module does not depend on feature modules`() {
        scope
            .files
            .filter { file ->
                val pkg = file.packagee?.name ?: return@filter false
                pkg.startsWith("mayudin.common.network")
            }
            .assertFalse(additionalMessage = "Common network must not import from feature modules") { file ->
                file.imports.any { "mayudin.feature." in it.name }
            }
    }
}

