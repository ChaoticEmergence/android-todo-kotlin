package com.example.tood_lsit_2_the_revenge

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.recyclerview.widget.RecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TaskFlowE2ETest {

    @get:Rule
    val activityRule = ActivityScenarioRule(
        com.example.tood_lsit_2_the_revenge.view.TaskListActivity::class.java
    )

    // Le swipe pour supprimer ne fonctionne pas dans les tests E2E, c'est un problème connu
    // Par contre il fonctionne physiquement et le delete fonctionne dans les tests unitaires
    @Test
    fun parcoursComplet_creerVerifierEditer() {
        onView(withId(R.id.fabAddTask)).perform(click())

        onView(withId(R.id.etTaskTitle))
            .perform(replaceText("Tâche E2E"), closeSoftKeyboard())
        onView(withId(R.id.etTaskDescription))
            .perform(replaceText("Description de test"), closeSoftKeyboard())

        onView(withId(R.id.btnSave)).perform(click())

        onView(withId(R.id.recyclerViewTasks))
            .check(matches(hasDescendant(withText("Tâche E2E"))))

        onView(withId(R.id.recyclerViewTasks))
            .perform(
                RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                    hasDescendant(withText("Tâche E2E")),
                    click()
                )
            )

        onView(withId(R.id.etTaskTitle))
            .check(matches(withText("Tâche E2E")))
        onView(withId(R.id.etTaskDescription))
            .check(matches(withText("Description de test")))

        onView(withId(R.id.btnCancel)).perform(click())

        onView(withId(R.id.recyclerViewTasks))
            .check(matches(hasDescendant(withText("Tâche E2E"))))
    }
}