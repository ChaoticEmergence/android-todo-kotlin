package com.example.tood_lsit_2_the_revenge.presenter

import com.example.tood_lsit_2_the_revenge.contract.TaskListContract
import com.example.tood_lsit_2_the_revenge.model.Category
import com.example.tood_lsit_2_the_revenge.model.Priority
import com.example.tood_lsit_2_the_revenge.model.Task
import com.example.tood_lsit_2_the_revenge.repository.TaskRepository
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class TaskListPresenterTest {

    private lateinit var mockVue: TaskListContract.View
    private lateinit var mockRepository: TaskRepository
    private lateinit var presenter: TaskListPresenter

    private val tachesExemple = listOf(
        Task(
            id = "1",
            title = "Marcel",
            description = "Devenir Marcel",
            category = Category.PERSONAL,
            priority = Priority.HIGH,
            isDone = false,
            dateCreation = 1000L
        ),
        Task(
            id = "2",
            title = "Bob",
            description = "Bobjour!",
            category = Category.SCHOOL,
            priority = Priority.MEDIUM,
            isDone = false,
            dateCreation = 2000L
        )
    )

    @Before
    fun setUp() {
        mockVue = mock()
        mockRepository = mock()
        whenever(mockRepository.filter(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(tachesExemple)
        presenter = TaskListPresenter(mockVue, mockRepository)
    }

    @Test
    fun onViewCreated_afficheLessTaches() {
        presenter.onViewCreated()
        verify(mockVue).displayTasks(tachesExemple)
    }

    @Test
    fun onAddTaskClicked_navigueAvecIdNull() {
        presenter.onAddTaskClicked()
        verify(mockVue).navigateToTask(null)
    }

    @Test
    fun onTaskClicked_navigueAvecIdDeLaTache() {
        presenter.onTaskClicked("1")
        verify(mockVue).navigateToTask("1")
    }

    @Test
    fun onDeleteTask_supprimeEtRafraichit() {
        presenter.onDeleteTask("1")
        verify(mockRepository).delete("1")
        verify(mockVue).displayTasks(any())
    }

    @Test
    fun onTaskDoneClicked_appelleUpdateEtRafraichit() {
        val tache = tachesExemple[0]
        whenever(mockRepository.getById("1")).thenReturn(tache)
        presenter.onTaskDoneClicked("1")
        verify(mockRepository).update(any())
        verify(mockVue).displayTasks(any())
    }

    @Test
    fun onTaskDoneClicked_tacheInexistante_neModifiePas() {
        whenever(mockRepository.getById("999")).thenReturn(null)
        presenter.onTaskDoneClicked("999")
        verify(mockRepository, never()).update(any())
    }

    @Test
    fun onViewDestroyed_vueNestPlusAppelee() {
        presenter.onViewDestroyed()
        presenter.onAddTaskClicked()
        verify(mockVue, never()).navigateToTask(any())
    }
}