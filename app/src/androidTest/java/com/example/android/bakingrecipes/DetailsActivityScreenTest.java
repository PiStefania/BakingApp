package com.example.android.bakingrecipes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class DetailsActivityScreenTest {

    public static final String RECIPE_INSTR = "Recipe Introduction";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init(){
        //First item to be clicked
        onView(withId(R.id.content)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }

    @Test
    public void clickRecyclerView_opensIngredientsFragment() {
        //First item to be clicked
        onView(withId(R.id.content_details)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks DetailsActivity has correct first element
        onView(withId(R.id.content_ingredients)).check(matches(hasDescendant(withText("Ingredient"))));
    }

    @Test
    public void clickRecyclerView_opensRecipeStepsFragment() {
        //First item to be clicked
        onView(withId(R.id.content_details)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Checks DetailsActivity has correct first element
        onView(withId(R.id.instructions_step_recipe)).check(matches(withText(RECIPE_INSTR)));
    }
}
