package com.example.tood_lsit_2_the_revenge

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskFilterE2ETest {

    @get:Rule
    val activityRule = ActivityScenarioRule(
        com.example.tood_lsit_2_the_revenge.view.TaskListActivity::class.java
    )

    private fun creerTache(titre: String, categorie: String) {
        onView(withId(R.id.fabAddTask)).perform(click())
        onView(withId(R.id.etTaskTitle))
            .perform(replaceText(titre), closeSoftKeyboard())
        onView(withId(R.id.spinnerFormCategory)).perform(click())
        onView(withText(categorie)).perform(click())
        onView(withId(R.id.btnSave)).perform(click())
    }

    @Test
    fun filtrage_parCategorie_afficheBonnesTaches() {
        creerTache("Réunion équipe", "WORK")
        creerTache("Réviser examen", "SCHOOL")

        onView(withId(R.id.recyclerViewTasks))
            .check(matches(hasDescendant(withText("Réunion équipe"))))
        onView(withId(R.id.recyclerViewTasks))
            .check(matches(hasDescendant(withText("Réviser examen"))))

        onView(withId(R.id.spinnerCategory)).perform(click())
        onView(withText("WORK")).perform(click())

        onView(withId(R.id.recyclerViewTasks))
            .check(matches(hasDescendant(withText("Réunion équipe"))))
        onView(withId(R.id.recyclerViewTasks))
            .check(matches(not(hasDescendant(withText("Réviser examen")))))

        onView(withId(R.id.spinnerCategory)).perform(click())
        onView(withText("SCHOOL")).perform(click())

        onView(withId(R.id.recyclerViewTasks))
            .check(matches(hasDescendant(withText("Réviser examen"))))
        onView(withId(R.id.recyclerViewTasks))
            .check(matches(not(hasDescendant(withText("Réunion équipe")))))
    }
}