package com.jonathanstewart.androidtemplate.repodetailsactivity

import android.app.Instrumentation
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.data.Owner
import com.jonathanstewart.androidtemplate.MainActivity
import com.jonathanstewart.androidtemplate.R
import com.jonathanstewart.androidtemplate.repodetails.DetailsActivity
import com.jonathanstewart.androidtemplate.repodetails.DetailsSharedViewModel
import com.jonathanstewart.androidtemplate.test.base.ActivityTest
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class RepoDetailsActivityTest: KoinTest,ActivityTest<DetailsActivity>(DetailsActivity::class.java) {

    val i = 10
    val repo = GitRepo(
    repoName = "TestName" + i,
    repoDescription = "TestDesc" + i,
    stars = i*13,
    forks = i*9,
    issues = i*6,
    size = i*124,
    query = "TestQuery"+i,
    url = "http://www.google.com",
    owner = Owner(name = "TestOwnerName"+i, avatarUrl = "TestAvatarUrl"+i, id = i+1000,url = "TestURL" +i),
    id = i,
    language = "TestLanguage"+i
    )

    val am = Instrumentation.ActivityMonitor(MainActivity::class.java.name, null, true)
    val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, DetailsActivity::class.java)
    val sharedViewModel : DetailsSharedViewModel = mockk(relaxed = true)
    val sharedLiveData  = MutableLiveData<GitRepo>()

    @Before
    fun setuUp() {
        every { sharedViewModel.repoLiveData } returns sharedLiveData
        stopKoin()
        intent.putExtra("repo", repo)
        startKoin { modules(module{
            single { sharedViewModel }
        }) }
    }

    @Test
    fun checkViews() {
        startActivityWithIntent(intent)
        Espresso.onView(withId(R.id.exit_button))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.open_in_browser_button))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repoCardView))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(withId(R.id.repoName))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repoName))
            .check(ViewAssertions.matches(withText(repo.repoName)))

        Espresso.onView(withId(R.id.repoDescription))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repoDescription))
            .check(ViewAssertions.matches(withText(repo.repoDescription)))

        Espresso.onView(withId(R.id.repo_forks))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repo_forks))
            .check(ViewAssertions.matches(withText(repo.forks.toString())))

        Espresso.onView(withId(R.id.repo_issues))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repo_issues))
            .check(ViewAssertions.matches(withText(repo.issues.toString())))

        Espresso.onView(withId(R.id.repo_language))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repo_language))
            .check(ViewAssertions.matches(withText(repo.language)))

        Espresso.onView(withId(R.id.repo_size))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repo_size))
            .check(ViewAssertions.matches(withText(repo.size.toString())))

        Espresso.onView(withId(R.id.repo_url))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repo_url))
            .check(ViewAssertions.matches(withText(repo.url)))

        Espresso.onView(withId(R.id.repoStars))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.repoStars))
            .check(ViewAssertions.matches(withText(repo.stars.toString())))

        Espresso.onView(withId(R.id.owner_name))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.owner_name))
            .check(ViewAssertions.matches(withText(repo.owner.name)))

        Espresso.onView(withId(R.id.owner_avatar_view))
            .check(ViewAssertions.matches(isDisplayed()))

        Espresso.onView(withId(R.id.view_profile_button))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.view_profile_button))
            .check(ViewAssertions.matches(withText("View Profile On Github.com")))

        InstrumentationRegistry.getInstrumentation().addMonitor(am)

        Espresso.onView(withId(R.id.exit_button)).perform(click())
        assertTrue(InstrumentationRegistry.getInstrumentation().checkMonitorHit(am, 1));
    }
}