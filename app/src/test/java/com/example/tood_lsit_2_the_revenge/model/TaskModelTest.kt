package com.example.tood_lsit_2_the_revenge.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskModelTest {

    @Test
    fun creerTache_proprietesCorrectes() {
        val tache = Task(
            id = "BonjourBaguette123",
            title = "Exister",
            description = "Decrire",
            category = Category.WORK,
            priority = Priority.HIGH,
            isDone = false,
            dateCreation = 1000L
        )

        assertEquals("BonjourBaguette123", tache.id)
        assertEquals("Exister", tache.title)
        assertEquals("Decrire", tache.description)
        assertEquals(Category.WORK, tache.category)
        assertEquals(Priority.HIGH, tache.priority)
        assertFalse(tache.isDone)
        assertEquals(1000L, tache.dateCreation)
    }

    @Test
    fun creerTache_sansId_genereUuidUnique() {
        val tache1 = Task(
            title = "Gaetan Tanguay",
            description = "Samuel Canuel",
            category = Category.OTHER,
            priority = Priority.MEDIUM,
            isDone = false,
            dateCreation = 1000L
        )
        val tache2 = Task(
            title = "Guy Guité",
            description = "Gilles Gillette",
            category = Category.SCHOOL,
            priority = Priority.HIGH,
            isDone = false,
            dateCreation = 2000L
        )

        assertNotNull(tache1.id)
        assertNotNull(tache2.id)
        assertNotEquals(tache1.id, tache2.id)
    }

    @Test
    fun copyAvecIsDoneInverse_conserveAutresProprietes() {
        val originale = Task(
            id = "bob19",
            title = "Edgy truc",
            description = "Incendiary claim about human nature",
            category = Category.SCHOOL,
            priority = Priority.MEDIUM,
            isDone = false,
            dateCreation = 3000L
        )

        val modifiee = originale.copy(isDone = !originale.isDone)

        assertTrue(modifiee.isDone)
        assertEquals(originale.id, modifiee.id)
        assertEquals(originale.title, modifiee.title)
        assertEquals(originale.description, modifiee.description)
        assertEquals(originale.category, modifiee.category)
        assertEquals(originale.priority, modifiee.priority)
        assertEquals(originale.dateCreation, modifiee.dateCreation)
    }

    @Test
    fun deuxTachesMemesDonnees_sontEgales() {
        val tache1 = Task(
            id = "37dignitaires",
            title = "Timothée Lamothe",
            description = "Bruno Chiasson",
            category = Category.OTHER,
            priority = Priority.LOW,
            isDone = true,
            dateCreation = 4000L
        )
        val tache2 = Task(
            id = "37dignitaires",
            title = "Timothée Lamothe",
            description = "Bruno Chiasson",
            category = Category.OTHER,
            priority = Priority.LOW,
            isDone = true,
            dateCreation = 4000L
        )

        assertEquals(tache1, tache2)
    }

    @Test
    fun enumCategorie_contientToutesLesValeurs() {
        val categories = Category.entries

        assertEquals(4, categories.size)
        assertTrue(categories.contains(Category.WORK))
        assertTrue(categories.contains(Category.PERSONAL))
        assertTrue(categories.contains(Category.SCHOOL))
        assertTrue(categories.contains(Category.OTHER))
    }
}