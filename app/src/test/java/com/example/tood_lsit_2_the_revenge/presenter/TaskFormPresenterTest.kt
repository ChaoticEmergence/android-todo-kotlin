package com.example.tood_lsit_2_the_revenge.presenter

import com.example.tood_lsit_2_the_revenge.contract.TaskFormContract
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import com.example.tood_lsit_2_the_revenge.repository.TaskRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TaskFormPresenterTest {

    private lateinit var mockVue: TaskFormContract.View
    private lateinit var mockRepository: TaskRepository
    private lateinit var presenter: TaskFormPresenter

    private val tacheExistante = Task(
        id = "Roger666",
        title = "Serge438",
        description = "Serge, mais mieux",
        category = Category.WORK,
        priority = Priority.HIGH,
        isDone = false,
        dateCreation = 5000L
    )

    @Before
    fun setUp() {
        mockVue = mock()
        mockRepository = mock()
        presenter = TaskFormPresenter(mockVue, mockRepository)
    }

    @Test
    fun onViewCreated_sansId_afficheTacheVide() {
        presenter.onViewCreated(null)
        verify(mockVue).displayTask(any())
    }

    @Test
    fun onViewCreated_avecId_afficheLaTacheExistante() {
        whenever(mockRepository.getById("Roger666")).thenReturn(tacheExistante)
        presenter.onViewCreated("Roger666")
        verify(mockVue).displayTask(tacheExistante)
    }

    @Test
    fun onSaveTask_titreVide_afficherErreur() {
        presenter.onSaveTask(null, "", "bla", Category.PERSONAL, Priority.LOW)
        verify(mockVue).showTitleError()
        verify(mockRepository, never()).add(any())
        verify(mockVue, never()).navigateToList()
    }

    @Test
    fun onSaveTask_titreEspaces_afficherErreur() {
        presenter.onSaveTask(null, "   ", "espaces", Category.OTHER, Priority.MEDIUM)
        verify(mockVue).showTitleError()
        verify(mockRepository, never()).add(any())
    }

    @Test
    fun onSaveTask_titreValide_creeEtNavigue() {
        presenter.onSaveTask(null, "Cyber Claude", "No escape", Category.SCHOOL, Priority.HIGH)
        verify(mockRepository).add(any())
        verify(mockVue).navigateToList()
    }

    @Test
    fun onSaveTask_modificationValide_metAJourEtNavigue() {
        whenever(mockRepository.getById("Roger666")).thenReturn(tacheExistante)
        presenter.onSaveTask("Roger666", "Roger667", "The neighbor of the beast", Category.PERSONAL, Priority.LOW)
        verify(mockRepository).update(any())
        verify(mockVue).navigateToList()
    }

    @Test
    fun onCancelClicked_navigueVersLaListe() {
        presenter.onCancelClicked()
        verify(mockVue).navigateToList()
    }
}