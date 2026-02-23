package mayudin.feature.repos.presentation.ui

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.TextFieldValue
import mayudin.feature.repos.presentation.model.UiState
import org.junit.Rule
import org.junit.Test

class ReposScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSearchBarDisplaysHint() {
        composeTestRule.setContent {
            SearchBar(
                searchText = remember { mutableStateOf(TextFieldValue("")) }.value,
                onSearchTextChanged = {},
                hint = "Search Repos",
            )
        }

        composeTestRule.onNodeWithText("Search Repos").assertIsDisplayed()
    }

    @Test
    fun testSearchBarUpdatesText() {
        val searchText = mutableStateOf(TextFieldValue(""))

        composeTestRule.setContent {
            SearchBar(
                searchText = searchText.value,
                onSearchTextChanged = { searchText.value = it },
                hint = "Search Repos",
            )
        }

        composeTestRule.onNodeWithText("").performTextInput("New Repo")
        composeTestRule.onNodeWithText("New Repo").assertIsDisplayed()
    }

    @Test
    fun testLoadingLayoutDisplaysProgressIndicator() {
        composeTestRule.setContent {
            LoadingLayout()
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun testSuccessLayoutDisplaysRepos() {
        val repos = listOf("Repo1", "Repo2", "Repo3")

        composeTestRule.setContent {
            SuccessLayout(
                repos = repos,
                owner = "testOwner",
                onRepoClicked = { _, _ -> },
            )
        }

        repos.forEach { repo ->
            composeTestRule.onNodeWithText(repo).assertIsDisplayed()
        }
    }

    @Test
    fun testSuccessLayoutDisplaysEmptyMessage() {
        composeTestRule.setContent {
            SuccessLayout(
                repos = emptyList(),
                owner = "testOwner",
                onRepoClicked = { _, _ -> },
            )
        }

        composeTestRule.onNodeWithText("No repositories found").assertIsDisplayed()
    }

    @Test
    fun testErrorLayoutDisplaysErrorMessage() {
        composeTestRule.setContent {
            ErrorLayout(message = "Network error occurred")
        }

        composeTestRule.onNodeWithText("Network error occurred").assertIsDisplayed()
    }

    @Test
    fun testReposScreenDisplaysLoadingState() {
        composeTestRule.setContent {
            ScreenLayout(
                uiState = UiState.Loading,
                searchText = TextFieldValue("test"),
                onSearchTextChanged = {},
                onRepoClicked = { _, _ -> },
            )
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun testReposScreenDisplaysSuccessState() {
        composeTestRule.setContent {
            ScreenLayout(
                uiState = UiState.Success(owner = "testOwner", repos = listOf("Repo1", "Repo2")),
                searchText = TextFieldValue("testOwner"),
                onSearchTextChanged = {},
                onRepoClicked = { _, _ -> },
            )
        }

        composeTestRule.onNodeWithText("Repo1").assertIsDisplayed()
        composeTestRule.onNodeWithText("Repo2").assertIsDisplayed()
    }

    @Test
    fun testReposScreenDisplaysErrorState() {
        composeTestRule.setContent {
            ScreenLayout(
                uiState = UiState.Error(message = "Failed to fetch repositories"),
                searchText = TextFieldValue("testOwner"),
                onSearchTextChanged = {},
                onRepoClicked = { _, _ -> },
            )
        }

        composeTestRule.onNodeWithText("Failed to fetch repositories").assertIsDisplayed()
    }
}
