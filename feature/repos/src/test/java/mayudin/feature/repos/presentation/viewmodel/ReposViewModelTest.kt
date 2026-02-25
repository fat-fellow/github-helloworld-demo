package mayudin.feature.repos.presentation.viewmodel

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mayudin.feature.repos.domain.usecase.ReposUseCase
import mayudin.feature.repos.presentation.model.RepoEffect
import mayudin.feature.repos.presentation.model.UiState
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReposViewModelTest {

    private lateinit var viewModel: ReposViewModel
    private lateinit var reposUseCase: ReposUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        reposUseCase = mockk()
        viewModel = ReposViewModel(reposUseCase)
    }

    @Test
    fun `uiState emits Idle initially`() = testScope.runTest {
        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())
        }
    }

    @Test
    fun `uiState emits Loading then Success on non-blank query`() = testScope.runTest {
        val query = "test"
        every { reposUseCase(query) } returns flowOf(listOf("Repo1", "Repo2"))

        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            assertTrue(awaitItem() is UiState.Loading)

            val success = awaitItem() as UiState.Success
            assertEquals(query, success.owner)
            assertEquals(listOf("Repo1", "Repo2"), success.repos)
        }
    }

    @Test
    fun `uiState emits Loading then two Success emissions on background refresh`() = testScope.runTest {
        val query = "test"
        every { reposUseCase(query) } returns flowOf(
            listOf("OldRepo"),
            listOf("OldRepo", "NewRepo"),
        )

        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            assertTrue(awaitItem() is UiState.Loading)
            assertEquals(listOf("OldRepo"), (awaitItem() as UiState.Success).repos)
            assertEquals(listOf("OldRepo", "NewRepo"), (awaitItem() as UiState.Success).repos)
        }
    }

    @Test
    fun `uiState emits Loading then Error when use case throws`() = testScope.runTest {
        val query = "test"
        every { reposUseCase(query) } returns flow { throw Throwable("Network error") }

        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            assertTrue(awaitItem() is UiState.Loading)

            val error = awaitItem() as UiState.Error
            assertEquals("Network error", error.message)
        }
    }

    @Test
    fun `uiState returns to Idle when query becomes blank`() = testScope.runTest {
        val query = "test"
        every { reposUseCase(query) } returns flowOf(listOf("Repo1"))

        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())

            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()
            awaitItem() // Loading
            assertTrue(awaitItem() is UiState.Success)

            viewModel.onSearchTextChanged("")
            advanceUntilIdle()
            assertEquals(UiState.Idle, awaitItem())
        }
    }

    @Test
    fun `onRepoClicked emits NavigateToInfo effect`() = testScope.runTest {
        viewModel.viewEffects.test {
            viewModel.onRepoClicked("owner", "repo")

            val effect = awaitItem() as RepoEffect.NavigateToInfo
            assertEquals("owner", effect.owner)
            assertEquals("repo", effect.repo)
        }
    }

    @Test
    fun `onRepoClicked emits correct effect for each click`() = testScope.runTest {
        viewModel.viewEffects.test {
            viewModel.onRepoClicked("owner1", "repo1")
            (awaitItem() as RepoEffect.NavigateToInfo).also {
                assertEquals("owner1", it.owner)
                assertEquals("repo1", it.repo)
            }

            viewModel.onRepoClicked("owner2", "repo2")
            (awaitItem() as RepoEffect.NavigateToInfo).also {
                assertEquals("owner2", it.owner)
                assertEquals("repo2", it.repo)
            }
        }
    }
}
