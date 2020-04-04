import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.data.Owner
import com.jonathanstewart.network.gitHubService.GitRepoNetworkService
import com.jonathanstewart.persistence.GitRepoPersistenceService
import com.jonathanstewart.repository.GitHubDataRepositoryImpl
import com.jonathanstewart.repository.Resource
import io.mockk.*
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class LoginRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var gitRepoNetworkService: GitRepoNetworkService
    lateinit var gitRepoPersistenceService: GitRepoPersistenceService
    val gitHubDataRepository by lazy { GitHubDataRepositoryImpl(gitRepoNetworkService, gitRepoPersistenceService) }
    val testQuery = "TESTQUERY"

    val testQueries by lazy { generateTestQueries() }
    val testRepos by lazy { generateTestRepos() }

    val latesQuery by lazy { MutableLiveData<String?>() }
    val allQueries by lazy { MutableLiveData<List<String>>() }

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        gitRepoNetworkService = mockk()
        gitRepoPersistenceService = mockk()
    }

    @Test
    fun testGetLatestQuery() {
        every { gitRepoPersistenceService.getLatestQuery() } returns latesQuery
        gitHubDataRepository.getLatestQuery().observeForever { }
        latesQuery.postValue(testQuery)
        assertEquals(testQuery, gitHubDataRepository.getLatestQuery().value)
    }

    @Test
    fun testGetQueries() {
        every { gitRepoPersistenceService.getAllQueries() } returns allQueries
        gitHubDataRepository.getQueries().observeForever { }
        allQueries.postValue(testQueries)
        assertEquals(testQueries, gitHubDataRepository.getQueries().value)
    }

    @Test
    fun testNewQuery() {
        coEvery { gitRepoPersistenceService.addQuery(any()) } just runs
        runBlocking { gitHubDataRepository.newQuery(testQuery) }
        coVerify { gitRepoPersistenceService.addQuery(testQuery) }
    }

    @Test
    fun testRepoSearchDataInDBNewQuery() {
        val newQuery = "NEWTESTQUERY"
        val newRepos = generateTestRepos(newQuery)
        coEvery{gitRepoPersistenceService.getAllRepos()} returns testRepos andThen newRepos
        coEvery{gitRepoNetworkService.searchForRepos(any())} returns newRepos
        coEvery{gitRepoPersistenceService.addRepoSearchResults(any(),any())} just runs
        val data = runBlocking { gitHubDataRepository.repoSearch(newQuery) }
        assertEquals(newRepos, data.value?.data)
    }

    @Test
    fun testRepoSearchDataInDBNewQueryException() {
        val newQuery = "NEWTESTQUERY"
        coEvery{gitRepoPersistenceService.getAllRepos()} returns testRepos
        coEvery{gitRepoNetworkService.searchForRepos(any())} answers  {throw Exception("TESTEXCEPTION") }
        coEvery{gitRepoPersistenceService.addRepoSearchResults(any(), any())} just runs
        val data = runBlocking { gitHubDataRepository.repoSearch(newQuery) }
        assertEquals(testRepos, data.value?.data)
        assertEquals(data.value?.status, Resource.Status.ERROR)
    }

    @Test
    fun testRepoSearchNoDataInDBNewQueryException() {
        val newQuery = "NEWTESTQUERY"
        coEvery{gitRepoPersistenceService.getAllRepos()} returns ArrayList()
        coEvery{gitRepoNetworkService.searchForRepos(any())} answers  {throw Exception("TESTEXCEPTION") }
        coEvery{gitRepoPersistenceService.addRepoSearchResults(any(), any())} just runs
        val data = runBlocking { gitHubDataRepository.repoSearch(newQuery) }
        assertEquals(ArrayList<GitRepo>(), data.value?.data)
        assertEquals(data.value?.status, Resource.Status.ERROR)
    }




    fun generateTestQueries(): ArrayList<String> {
        val testRepos = ArrayList<String>()
        for (i in 0..10) {
            testRepos.add(testQuery + i)
        }
        return testRepos
    }

    fun generateTestRepos(query: String? = testQuery): ArrayList<GitRepo> {
        val testRepos = ArrayList<GitRepo>()
        for (i in 0..10) {
            testRepos.add(
                GitRepo(
                    repoName = "TestName" + i,
                    repoDescription = "TestDesc" + i,
                    stars = i * 13,
                    forks = i * 9,
                    issues = i * 6,
                    size = i * 124,
                    query = query,
                    url = "TestUrl" + i,
                    owner = Owner(
                        name = "TestOwnerName" + i,
                        avatarUrl = "TestAvatarUrl" + i,
                        id = i + 1000,
                        url = "TestURL" + i
                    ),
                    id = i,
                    language = "TestLanguage" + i
                )
            )
        }
        return testRepos
    }
}




