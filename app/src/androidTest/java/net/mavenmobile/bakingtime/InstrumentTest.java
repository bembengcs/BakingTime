package net.mavenmobile.bakingtime;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import net.mavenmobile.bakingtime.activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Same as Espresso's BasicSample, but with an Idling Resource to help with synchronization.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class InstrumentTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void textViewRecipeTest() {
        onView(ViewMatchers.withId(R.id.rv_recipe_card))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Brownies")).check(matches(isDisplayed()));
    }

    @Test
    public void recyclerIngredientTest() {
        onView(ViewMatchers.withId(R.id.rv_recipe_card))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.rv_ingredient))
                .perform(net.mavenmobile.bakingtime.Utils.ScrollToAction.betterScrollTo());
        onView(withText("Graham Cracker crumbs"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void recyclerStepTest() {
        onView(ViewMatchers.withId(R.id.rv_recipe_card))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.rv_step))
                .perform(net.mavenmobile.bakingtime.Utils.ScrollToAction.betterScrollTo())
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test public void checkExoPlayer() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.rv_recipe_card))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.rv_step))
                .perform(net.mavenmobile.bakingtime.Utils.ScrollToAction.betterScrollTo())
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(10000);
        onView(allOf(withId(R.id.player_view),
                withClassName(is(SimpleExoPlayerView.class.getName()))));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}