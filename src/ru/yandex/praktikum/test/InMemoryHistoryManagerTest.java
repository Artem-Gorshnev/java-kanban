package ru.yandex.praktikum.test;

import org.junit.jupiter.api.Test;
import ru.yandex.praktikum.manager.InMemoryHistoryManager;
import ru.yandex.praktikum.manager.InMemoryTaskManager;
import ru.yandex.praktikum.manager.TaskManager;
import ru.yandex.praktikum.tasks.StatusTask;
import ru.yandex.praktikum.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    @Test
    public void testHistoryManagerStoresPreviousTaskVersions() {
        // *убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
        Task task1 = new Task("Task 1", "Description 1", StatusTask.NEW);
        Task task2 = new Task("Task 2", "Description 2", StatusTask.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", StatusTask.DONE);

        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.getTaskById(0);
        manager.getTaskById(1);
        manager.getTaskById(2);

        List<Task> history = manager.getHistory();

        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
        assertEquals(3, history.size());
    }

    @Test
    public void addToHistory() {
        Task task1 = manager.createTask(new Task("Task 1", "Description 1", StatusTask.NEW));
        Task task2 = manager.createTask(new Task("Task 2", "Description 2", StatusTask.NEW));
        Task task3 = manager.createTask(new Task("Task 3", "Description 3", StatusTask.NEW));
        Task task4 = manager.createTask(new Task("Task 4", "Description 4", StatusTask.NEW));
        Task task5 = manager.createTask(new Task("Task 5", "Description 5", StatusTask.NEW));
        Task task6 = manager.createTask(new Task("Task 6", "Description 6", StatusTask.NEW));

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task6);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(6, history.size());
        inMemoryHistoryManager.add(task1);
        assertEquals(6, history.size());
    }

    @Test
    public void removeToHistory() {
        Task task1 = manager.createTask(new Task("Task 1", "Description 1", StatusTask.NEW));
        Task task2 = manager.createTask(new Task("Task 2", "Description 2", StatusTask.NEW));
        Task task3 = manager.createTask(new Task("Task 3", "Description 3", StatusTask.NEW));
        Task task4 = manager.createTask(new Task("Task 4", "Description 4", StatusTask.NEW));
        Task task5 = manager.createTask(new Task("Task 5", "Description 5", StatusTask.NEW));
        Task task6 = manager.createTask(new Task("Task 6", "Description 6", StatusTask.NEW));

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
        inMemoryHistoryManager.add(task4);
        inMemoryHistoryManager.add(task5);
        inMemoryHistoryManager.add(task6);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(6, history.size());
        inMemoryHistoryManager.add(task1);
        assertEquals(6, history.size());
        inMemoryHistoryManager.remove(0);
        List<Task> history2 = inMemoryHistoryManager.getHistory();
        assertEquals(5, history2.size());
    }

    @Test
    public void testAddTaskToHistory() { // Тест на добавление задачи в историю в InMemoryHistoryManager:
        Task task = new Task("Задача 1", "Описание 1", StatusTask.NEW);

        inMemoryHistoryManager.add(task);

        List<Task> history = inMemoryHistoryManager.getHistory();
        assertTrue(history.contains(task));
    }

    @Test
    public void testRemoveTaskFromHistory() { // Тест на удаление задачи из истории в InMemoryHistoryManager:
        Task task = new Task("Задача 1", "Описание 1", StatusTask.NEW);

        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.remove(task.getIdNumber());

        List<Task> history = inMemoryHistoryManager.getHistory();
        assertFalse(history.contains(task));
    }

}