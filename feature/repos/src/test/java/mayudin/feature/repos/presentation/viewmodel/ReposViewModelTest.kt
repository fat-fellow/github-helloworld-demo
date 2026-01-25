package mayudin.feature.repos.presentation.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun `uiState emits Idle state initially`() = testScope.runTest {
        viewModel.uiState.test {
            assertEquals(UiState.Idle, awaitItem())
        }
    }

    @Test
    fun `uiState emits Success state with results when query is not blank`() = testScope.runTest {
        val query = "test"
        coEvery { reposUseCase(query) } returns listOf("Repo1", "Repo2")

        viewModel.uiState.test {
            // Initial Idle state
            assertEquals(UiState.Idle, awaitItem())

            // Trigger search
            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            // Loading state
            assertTrue(awaitItem() is UiState.Loading)

            // Success state
            val successItem = awaitItem()
            assertTrue(successItem is UiState.Success)
            assertEquals("test", successItem.owner)
            assertEquals(listOf("Repo1", "Repo2"), successItem.repos)
        }
    }

    @Test
    fun `uiState emits Loading state when query is not blank`() = testScope.runTest {
        val query = "test"
        coEvery { reposUseCase(query) } returns listOf("Repo1", "Repo2")

        viewModel.uiState.test {
            // Initial Idle state
            assertEquals(UiState.Idle, awaitItem())

            // Trigger search
            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            // Loading state should be emitted
            val loadingItem = awaitItem()
            assertTrue(loadingItem is UiState.Loading)

            // May also emit Success, which is fine - we just needed to verify Loading was emitted
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Error state when use case throws exception`() = testScope.runTest {
        val query = "test"
        val exception = Throwable("An error occurred")
        coEvery { reposUseCase(query) } throws exception

        viewModel.uiState.test {
            // Initial Idle state
            assertEquals(UiState.Idle, awaitItem())

            // Trigger search
            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            // Loading state
            assertTrue(awaitItem() is UiState.Loading)

            // Error state
            val item = awaitItem()
            assertTrue(item is UiState.Error)
            assertEquals("An error occurred", item.message)
        }
    }

    @Test
    fun `uiState returns to Idle state when query becomes blank`() = testScope.runTest {
        val query = "test"
        coEvery { reposUseCase(query) } returns listOf("Repo1", "Repo2")

        viewModel.uiState.test {
            // Initial Idle state
            assertEquals(UiState.Idle, awaitItem())

            // Trigger search
            viewModel.onSearchTextChanged(query)
            advanceUntilIdle()

            // Loading state
            assertTrue(awaitItem() is UiState.Loading)

            // Success state
            assertTrue(awaitItem() is UiState.Success)

            // Clear search
            viewModel.onSearchTextChanged("")
            advanceUntilIdle()

            // Back to Idle state
            assertEquals(UiState.Idle, awaitItem())
        }
    }

    @Test
    fun `onRepoClicked emits NavigateToInfo effect`() = testScope.runTest {
        val owner = "testOwner"
        val repo = "testRepo"

        viewModel.viewEffects.test {
            viewModel.onRepoClicked(owner, repo)

            val effect = awaitItem()
            assertTrue(effect is RepoEffect.NavigateToInfo)
            assertEquals(owner, effect.owner)
            assertEquals(repo, effect.repo)
        }
    }

    @Test
    fun `onRepoClicked with different parameters emits correct NavigateToInfo effect`() = testScope.runTest {
        viewModel.viewEffects.test {
            viewModel.onRepoClicked("owner1", "repo1")
            val effect1 = awaitItem()
            assertTrue(effect1 is RepoEffect.NavigateToInfo)
            assertEquals("owner1", effect1.owner)
            assertEquals("repo1", effect1.repo)

            viewModel.onRepoClicked("owner2", "repo2")
            val effect2 = awaitItem()
            assertTrue(effect2 is RepoEffect.NavigateToInfo)
            assertEquals("owner2", effect2.owner)
            assertEquals("repo2", effect2.repo)
        }
    }
}
