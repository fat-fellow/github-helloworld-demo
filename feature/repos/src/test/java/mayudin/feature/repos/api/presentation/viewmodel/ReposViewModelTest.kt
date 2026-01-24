package mayudin.feature.repos.api.presentation.viewmodel

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import mayudin.common.utils.domain.Resultat
import mayudin.feature.repos.api.domain.usecase.ReposUseCase
import mayudin.feature.repos.api.presentation.model.UiState
import org.junit.Before
import org.junit.Test
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ReposViewModelTest {

    private lateinit var viewModel: ReposViewModel
    private lateinit var reposUseCase: ReposUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        reposUseCase = mockk()
        viewModel = ReposViewModel(reposUseCase)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `uiState emits empty list - when query is blank`() {
        testScope.runTest {
            viewModel.uiState.test {
                assertEquals(Resultat.success(UiState(emptyList())), awaitItem())
            }
        }
    }

    @Test
    fun `uiState - emits results from use case - when query is not blank`() = testScope.runTest {
        val query = "test"
        coEvery { reposUseCase(query) } returns listOf("Repo1", "Repo2")

        viewModel.onSearchTextChanged(query)

        viewModel.uiState.test {
            skipItems(1) // Skip initial empty state
            // Skip loading state
            val loadingItem = awaitItem()
            assertEquals(true, loadingItem.isLoading)
            // Get success state
            val successItem = awaitItem()
            assertEquals(Resultat.success(UiState(listOf("Repo1", "Repo2"))), successItem)
        }
    }

    @Test
    fun `uiState - emits loading state - when query is not blank`() = testScope.runTest {
        val query = "test"
        coEvery { reposUseCase(query) } returns listOf("Repo1", "Repo2")

        viewModel.onSearchTextChanged(query)

        viewModel.uiState.test {
            skipItems(1) // Skip initial empty state
            val item = awaitItem()
            assertEquals(true, item.isLoading)
        }
    }

    @Test
    fun `uiState - emits error state - when use case returns error`() = testScope.runTest {
        val query = "test"
        val exception = Throwable("An error occurred")
        coEvery { reposUseCase(query) } throws exception

        viewModel.onSearchTextChanged(query)

        viewModel.uiState.test {
            skipItems(1) // Skip initial empty state
            // Skip loading state
            skipItems(1)
            // Get error state
            val item = awaitItem()
            assertEquals(true, item.isFailure)
            assertEquals("An error occurred", item.exceptionOrNull()?.message)
        }
    }
}
